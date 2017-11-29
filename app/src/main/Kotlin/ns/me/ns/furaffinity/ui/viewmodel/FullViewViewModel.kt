package ns.me.ns.furaffinity.ui.viewmodel

import android.animation.ValueAnimator
import android.app.Application
import android.content.Intent
import android.databinding.Observable
import android.databinding.ObservableField
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.view.ViewPager
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.repository.FavoriteRepository
import ns.me.ns.furaffinity.repository.GalleryRepository
import ns.me.ns.furaffinity.repository.SubmissionRepository
import ns.me.ns.furaffinity.repository.model.ViewInterface
import ns.me.ns.furaffinity.ui.InterceptTouchViewPager
import ns.me.ns.furaffinity.ui.activity.GalleryActivity
import ns.me.ns.furaffinity.ui.adapter.pager.FullViewPagerAdapter
import ns.me.ns.furaffinity.util.BitmapUtil
import ns.me.ns.furaffinity.util.LogUtil
import ns.me.ns.furaffinity.util.ToastUtil
import javax.inject.Inject

class FullViewViewModel @Inject constructor(application: Application) : AbstractBaseViewModel(application) {

    enum class Type {
        Submission, Favorite, Gallery
    }

    enum class DragState {
        Dragging, Finish
    }

    @Inject
    lateinit var favoriteRepository: FavoriteRepository

    @Inject
    lateinit var submissionRepository: SubmissionRepository

    @Inject
    lateinit var galleryRepository: GalleryRepository

    val fullViewPagerAdapter: FullViewPagerAdapter by lazy {
        FullViewPagerAdapter(application).apply {
            onPhotoTap = {
                systemUISubject.onNext(Unit)
            }
        }
    }

    val pageChangeSubject: PublishSubject<Int> = PublishSubject.create()

    val layoutChangeSubject: PublishSubject<Triple<DragState, Float, Float>> = PublishSubject.create()

    val favoriteChangeSubject: PublishSubject<Boolean> = PublishSubject.create()

    var returnLayoutAnimator: ValueAnimator? = null

    var scrollState: Int = ViewPager.SCROLL_STATE_IDLE

    val imageChangeSubject: PublishSubject<Bitmap> = PublishSubject.create()

    private val onPropertyChangedCallback: Observable.OnPropertyChangedCallback by lazy {
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: Observable?, id: Int) {
                when (observable) {
                    view -> {
                        checkFavorite()
                        (view.get()?.image?.get() as? BitmapDrawable)?.bitmap?.let {
                            imageChangeSubject.onNext(it)
                        }

                    }
                }
            }
        }
    }

    var view: ObservableField<ViewInterface> = ObservableField<ViewInterface>().apply {
        addOnPropertyChangedCallback(onPropertyChangedCallback)
    }

    val onInterceptDragListener = object : InterceptTouchViewPager.OnInterceptDragListener {

        override fun onDragging(dx: Float, dy: Float) {
            if (returnLayoutAnimator?.isRunning == true) return@onDragging
            if (scrollState != ViewPager.SCROLL_STATE_IDLE) return@onDragging
            layoutChangeSubject.onNext(Triple(DragState.Dragging, dx, dy))
        }

        override fun onFinishDrag(dx: Float, dy: Float) {
            if (returnLayoutAnimator?.isRunning == true) return@onFinishDrag
            layoutChangeSubject.onNext(Triple(DragState.Finish, dx, dy))
        }

    }

    val onPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(state: Int) {
            scrollState = state
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            val item = fullViewPagerAdapter.getData(position)
            view.set(item)
        }

    }

    val onClickFloatingAction = View.OnClickListener { _ ->
        view.get()?.userElement?.get()?.account?.let {
            startActivitySubject.onNext(GalleryActivity.intent(context, it))
        }
    }

    fun createViewPagerItems(type:Type, viewId: Int, account: String? = null) {
        when (type) {
            Type.Submission -> {
                submissionRepository.getLocal()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            fullViewPagerAdapter.setData(it)
                            fullViewPagerAdapter.notifyDataSetChanged()
                            val position = fullViewPagerAdapter.findPosition(viewId)
                            if (position == 0) {
                                val item = fullViewPagerAdapter.getData(position)
                                view.set(item)
                            } else {
                                pageChangeSubject.onNext(position)
                            }
                        }, {
                            LogUtil.e(it)
                        })
            }
            Type.Favorite -> {
                favoriteRepository.getLocal()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            fullViewPagerAdapter.setData(it)
                            fullViewPagerAdapter.notifyDataSetChanged()
                            val position = fullViewPagerAdapter.findPosition(viewId)
                            if (position == 0) {
                                val item = fullViewPagerAdapter.getData(position)
                                view.set(item)
                            } else {
                                pageChangeSubject.onNext(position)
                            }
                        }, {
                            LogUtil.e(it)
                        })
            }
            FullViewViewModel.Type.Gallery -> {
                account ?: return
                galleryRepository.getLocal(account)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            fullViewPagerAdapter.setData(it)
                            fullViewPagerAdapter.notifyDataSetChanged()
                            val position = fullViewPagerAdapter.findPosition(viewId)
                            if (position == 0) {
                                val item = fullViewPagerAdapter.getData(position)
                                view.set(item)
                            } else {
                                pageChangeSubject.onNext(position)
                            }
                        }, {
                            LogUtil.e(it)
                        })
            }
            else -> {
                // 処理なし
            }
        }
    }

    fun changeFavorite() {
        val view = view.get() ?: return
        favoriteRepository.find(view.viewId)
                .flatMapCompletable { favoriteRepository.remove(view.viewId) }
                .onErrorResumeNext {
                    val bitmap = (view.image.get() as? BitmapDrawable)?.bitmap
                    return@onErrorResumeNext favoriteRepository.save(view, bitmap)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    checkFavorite()
                }, {
                    LogUtil.e(it)
                })

    }


    fun saveImage() {
        val view = view.get() ?: return@saveImage
        val bitmap = (view.image.get() as? BitmapDrawable)?.bitmap ?: return@saveImage
        BitmapUtil.write(view.viewId.toString(), bitmap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    ToastUtil.showToast(context, R.string.message_save_image_success)
                }, {
                    LogUtil.e(it)
                    ToastUtil.showToast(context, R.string.message_save_image_failed)
                })
    }

    fun shareImage() {
        val view = view.get() ?: return@shareImage
        val bitmap = (view.image.get() as? BitmapDrawable)?.bitmap ?: return@shareImage
        val uri = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, view.imageElement.get()?.alt, null)
        startActivitySubject.onNext(Intent.createChooser(Intent(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_STREAM, Uri.parse(uri))
                .setType("image/jpeg"), context.getString(R.string.message_share_image)))

    }

    fun playReturnLayoutAnimator(view: View, from: Float, to: Float) {
        returnLayoutAnimator = ValueAnimator.ofFloat(from, 0f)
        returnLayoutAnimator?.addUpdateListener { animation ->
            (animation.animatedValue as? Float)?.let {
                view.y = it
            }
        }
        returnLayoutAnimator?.setTarget(view)
        returnLayoutAnimator?.start()
    }

    private fun checkFavorite() {
        val view = view.get() ?: return@checkFavorite
        favoriteRepository.find(view.viewId)
                .map { true }
                .onErrorReturn { false }
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    favoriteChangeSubject.onNext(it)
                }, {
                    LogUtil.e(it)
                })

    }

}
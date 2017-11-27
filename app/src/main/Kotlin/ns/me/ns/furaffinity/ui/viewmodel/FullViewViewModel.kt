package ns.me.ns.furaffinity.ui.viewmodel

import android.app.Application
import android.content.Intent
import android.databinding.Observable
import android.databinding.ObservableField
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.ds.webapi.AppWebApiService
import ns.me.ns.furaffinity.exception.LoginRequiredException
import ns.me.ns.furaffinity.repository.FavoriteRepository
import ns.me.ns.furaffinity.repository.SubmissionRepository
import ns.me.ns.furaffinity.repository.model.local.Favorite
import ns.me.ns.furaffinity.repository.model.remote.Full
import ns.me.ns.furaffinity.ui.ObservableDrawableTarget
import ns.me.ns.furaffinity.ui.activity.LoginActivity
import ns.me.ns.furaffinity.ui.adapter.pager.FullViewPagerAdapter
import ns.me.ns.furaffinity.util.BitmapUtil
import ns.me.ns.furaffinity.util.LogUtil
import ns.me.ns.furaffinity.util.ToastUtil
import javax.inject.Inject

class FullViewViewModel @Inject constructor(application: Application) : AbstractBaseViewModel(application) {

    enum class Type {
        SUBMISSION, FAVORITE
    }

    @Inject
    lateinit var service: AppWebApiService

    @Inject
    lateinit var favoriteRepository: FavoriteRepository

    @Inject
    lateinit var submissionRepository: SubmissionRepository

    val fullViewPagerAdapter: FullViewPagerAdapter by lazy {
        FullViewPagerAdapter(application)
    }

    val imageChangeSubject: PublishSubject<Bitmap> = PublishSubject.create()

    val pageChangeSubject: PublishSubject<Int> = PublishSubject.create()

    var actionIconResId: ObservableField<Int> = ObservableField()

    private val onPropertyChangedCallback: Observable.OnPropertyChangedCallback by lazy {
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: Observable?, id: Int) {
                when (observable) {
                    viewId -> {
                        getFull()
                        checkFavorite()
                    }
                    image -> {
                        (image.get() as? BitmapDrawable)?.bitmap?.let {
                            imageChangeSubject.onNext(it)
                            imageChangeSubject.onComplete()
                        }
                    }
                    full -> {
                        load(full.get()?.imageElement?.src)
                    }
                }
            }
        }
    }

    var viewId: ObservableField<Int> = ObservableField<Int>().apply {
        addOnPropertyChangedCallback(onPropertyChangedCallback)
    }

    val full: ObservableField<Full> = ObservableField<Full>().apply {
        addOnPropertyChangedCallback(onPropertyChangedCallback)
    }

    val image: ObservableDrawableTarget = ObservableDrawableTarget().apply {
        addOnPropertyChangedCallback(onPropertyChangedCallback)
    }

    val onPhotoTapListener = OnPhotoTapListener { _, _, _ ->
        systemUISubject.onNext(Unit)
    }

    val onClickFavorite = View.OnClickListener { _ ->
        val viewId = viewId.get() ?: return@OnClickListener
        favoriteRepository.find(viewId)
                .flatMapCompletable { favoriteRepository.remove(viewId) }
                .onErrorResumeNext {
                    val favorite = Favorite()
                    favorite.viewId = viewId
                    favorite.src = full.get()?.imageElement?.src
                    favorite.alt = full.get()?.imageElement?.alt
                    val bitmap = (image.get() as? BitmapDrawable)?.bitmap
                    bitmap?.let {
                        favorite.imageData = BitmapUtil.decodeByteArray(it)
                    }
                    return@onErrorResumeNext favoriteRepository.save(favorite)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    checkFavorite()
                }, {
                    LogUtil.e(it)
                })

    }

    fun createViewPagerItems(type: Type, viewId: Int?) {
        when (type) {
            Type.SUBMISSION -> {
                submissionRepository.getLocal()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            fullViewPagerAdapter.items.addAll(it.map { FullViewPagerAdapter.ViewModel(it) })
                            fullViewPagerAdapter.notifyDataSetChanged()
                            val selection = fullViewPagerAdapter.items.indexOfFirst { it.viewId == viewId }
                            pageChangeSubject.onNext(selection)
                        }, {
                            LogUtil.e(it)
                        })
            }
            Type.FAVORITE -> {
                favoriteRepository.getLocal()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            fullViewPagerAdapter.items.addAll(it.map { FullViewPagerAdapter.ViewModel(it) })
                            fullViewPagerAdapter.notifyDataSetChanged()
                            val selection = fullViewPagerAdapter.items.indexOfFirst { it.viewId == viewId }
                            pageChangeSubject.onNext(selection)
                        }, {
                            LogUtil.e(it)
                        })
            }
        }
    }

    fun saveImage() {
        val bitmap = (image.get() as? BitmapDrawable)?.bitmap ?: return
        val viewId = viewId.get() ?: return
        BitmapUtil.write(viewId.toString(), bitmap)
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
        val bitmap = (image.get() as? BitmapDrawable)?.bitmap ?: return
        val uri = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, full.get()?.title, null)
        startActivitySubject.onNext(Intent.createChooser(Intent(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_STREAM, Uri.parse(uri))
                .setType("imageElement/jpeg"), context.getString(R.string.message_share_image)))

    }


    private fun load(url: String?) {
        Picasso.with(context).load(url).placeholder(image.get()).into(image)
    }

    private fun getFull() {
        val viewId = viewId.get() ?: return
        service.getFull(viewId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    full.set(it)
                }, {
                    LogUtil.e(it)
                    if (it is LoginRequiredException) {
                        startActivitySubject.onNext(LoginActivity.intent(context))
                    }
                })
    }

    private fun checkFavorite() {
        val viewId = viewId.get() ?: return
        favoriteRepository.find(viewId)
                .map { true }
                .onErrorReturn { false }
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    actionIconResId.set(if (it) R.drawable.fab_favorite_border else R.drawable.fab_favorite)
                }, {
                    LogUtil.e(it)
                })

    }

}
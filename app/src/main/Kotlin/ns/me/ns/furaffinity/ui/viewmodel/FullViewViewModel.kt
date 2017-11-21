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
import ns.me.ns.furaffinity.ds.remote.AppWebApiService
import ns.me.ns.furaffinity.ds.remote.model.impl.Full
import ns.me.ns.furaffinity.exception.LoginRequiredException
import ns.me.ns.furaffinity.logic.FavoriteLogic
import ns.me.ns.furaffinity.ui.ObservableDrawableTarget
import ns.me.ns.furaffinity.ui.activity.LoginActivity
import ns.me.ns.furaffinity.util.BitmapUtil
import ns.me.ns.furaffinity.util.LogUtil
import ns.me.ns.furaffinity.util.ToastUtil
import javax.inject.Inject

class FullViewViewModel @Inject constructor(application: Application) : AbstractBaseViewModel(application) {

    @Inject
    lateinit var service: AppWebApiService

    @Inject
    lateinit var favoriteLogic: FavoriteLogic

    val imageChangeSubject: PublishSubject<Bitmap> = PublishSubject.create()

    var actionIconResId: ObservableField<Int> = ObservableField()

    private val onPropertyChangedCallback: Observable.OnPropertyChangedCallback by lazy {
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: Observable?, id: Int) {
                when (observable) {
                    viewId -> {
                        val viewId = viewId.get() ?: return
                        getFull(viewId)
                        checkFavorite(viewId)
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
        favoriteLogic.isFavorite(viewId)
                .flatMap {
                    return@flatMap if (it) {
                        favoriteLogic.remove(viewId).toSingleDefault(false)
                    } else {
                        val bitmap = (image.get() as? BitmapDrawable)?.bitmap
                        favoriteLogic.save(viewId, full.get()?.imageElement?.src, full.get()?.imageElement?.alt, bitmap).toSingleDefault(true)

                    }
                }
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    actionIconResId.set(if (it) R.drawable.fab_favorite_border else R.drawable.fab_favorite)
                    ToastUtil.showToast(context, if (it) R.string.message_add_favorite else R.string.message_remove_favorite)
                }, {
                    LogUtil.e(it)
                })


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

    private fun getFull(viewId: Int) {

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

    private fun checkFavorite(viewId: Int) {
        favoriteLogic.isFavorite(viewId)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    actionIconResId.set(if (it) R.drawable.fab_favorite_border else R.drawable.fab_favorite)
                }, {
                    LogUtil.e(it)
                })

    }

}
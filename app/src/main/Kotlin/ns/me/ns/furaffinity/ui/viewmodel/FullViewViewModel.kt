package ns.me.ns.furaffinity.ui.viewmodel

import android.app.Application
import android.databinding.Observable
import android.databinding.ObservableField
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.ds.local.model.Favorite
import ns.me.ns.furaffinity.ds.remote.AppWebApiService
import ns.me.ns.furaffinity.ds.remote.model.impl.Full
import ns.me.ns.furaffinity.exception.LoginRequiredException
import ns.me.ns.furaffinity.pacakge.FavoriteLogic
import ns.me.ns.furaffinity.ui.ObservableDrawableTarget
import ns.me.ns.furaffinity.ui.activity.LoginActivity
import ns.me.ns.furaffinity.util.LogUtil
import ns.me.ns.furaffinity.util.ToastUtil
import javax.inject.Inject

class FullViewViewModel @Inject constructor(application: Application) : AbstractBaseViewModel(application) {

    @Inject
    lateinit var service: AppWebApiService

    @Inject
    lateinit var favoriteLogic: FavoriteLogic

    var viewId = ObservableField<Int>()

    val full = ObservableField<Full>()

    var favorite = ObservableField<Favorite>()

    val image = ObservableDrawableTarget()

    private val viewIdOnPropertyChangedCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: Observable?, id: Int) {
            favoriteLogic.find(viewId.get())
                    .observeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        favorite.set(it)
                    }, {
                        LogUtil.e(it)
                    })

            getFull()
        }
    }
    private val fullOnPropertyChangedCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: Observable?, id: Int) {
            load(full.get()?.imageElement?.src)
        }
    }

    init {
        viewId.addOnPropertyChangedCallback(viewIdOnPropertyChangedCallback)
        full.addOnPropertyChangedCallback(fullOnPropertyChangedCallback)
    }

    val onPhotoTapListener = OnPhotoTapListener { _, _, _ ->
        systemUISubject.onNext(Unit)
    }

    val onClickFavorite = View.OnClickListener { _ ->
        val viewId = viewId.get() ?: return@OnClickListener
        val imageElement = full.get()?.imageElement ?: return@OnClickListener

        favoriteLogic.save(viewId, imageElement.src, imageElement.alt, getImageBitmap())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    ToastUtil.showToast(context, R.string.message_add_favorite)
                }, {
                    LogUtil.e(it)
                })


    }

    fun getImageBitmap(): Bitmap? {
        return (image.get() as? BitmapDrawable)?.bitmap
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

}
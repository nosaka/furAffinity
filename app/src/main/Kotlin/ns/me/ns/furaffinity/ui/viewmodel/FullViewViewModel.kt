package ns.me.ns.furaffinity.ui.viewmodel

import android.app.Application
import android.databinding.ObservableField
import android.view.View
import com.github.chrisbanes.photoview.OnPhotoTapListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.datasouce.web.AppWebApiService
import ns.me.ns.furaffinity.datasouce.web.model.impl.Full
import ns.me.ns.furaffinity.exception.LoginRequiredException
import ns.me.ns.furaffinity.ui.activity.LoginActivity
import ns.me.ns.furaffinity.util.LogUtil
import ns.me.ns.furaffinity.util.ToastUtil
import javax.inject.Inject

class FullViewViewModel @Inject constructor(application: Application) : AbstractBaseViewModel(application) {

    @Inject
    lateinit var service: AppWebApiService

    var viewId: Int? = null

    val full = ObservableField<Full>()

    override fun onCreate() {
        super.onCreate()
        getFull()
    }

    val onPhotoTapListener = OnPhotoTapListener { _, _, _ ->
        systemUISubject.onNext(Unit)
    }

    val onClickFavorite = View.OnClickListener { view ->
        ToastUtil.showToast(context, R.string.message_add_favorite)
    }

    private fun getFull() {
        viewId ?: return

        service.getFull(viewId!!)
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
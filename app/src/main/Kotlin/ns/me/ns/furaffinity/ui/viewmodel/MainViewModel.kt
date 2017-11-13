package ns.me.ns.furaffinity.ui.viewmodel

import android.app.Application
import android.support.v7.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ns.me.ns.furaffinity.datasouce.web.AppWebApiService
import ns.me.ns.furaffinity.exception.LoginRequiredException
import ns.me.ns.furaffinity.ui.activity.FullViewActivity
import ns.me.ns.furaffinity.ui.activity.LoginActivity
import ns.me.ns.furaffinity.ui.adapter.OnEndScrollListener
import ns.me.ns.furaffinity.ui.adapter.recycler.ImageGalleryAdapter
import ns.me.ns.furaffinity.util.LogUtil
import javax.inject.Inject

class MainViewModel @Inject constructor(application: Application) : AbstractBaseViewModel(application) {

    @Inject
    lateinit var service: AppWebApiService

    val imageGalleryAdapter: ImageGalleryAdapter by lazy {
        ImageGalleryAdapter(application).apply {
            onItemClick = { _, data, view ->
                data.viewId?.let {
                    startActivitySubject.onNext(StartActivitySubject(view, FullViewActivity.intent(context, it)))
                }
            }
        }
    }

    private var pathMore: String? = ""

    fun getOnEndScrollListener(): OnEndScrollListener {
        return object : OnEndScrollListener() {
            override fun onEndScroll(recyclerView: RecyclerView, dx: Int, dy: Int) {
                getBrowse()
            }
        }
    }

    private fun getBrowse() {
        if (pathMore == null) {
            imageGalleryAdapter.setFooterDisplay(false)
            imageGalleryAdapter.notifyItemChanged(imageGalleryAdapter.itemCount)
            return
        }

        service.getMsgSubmissions(pathMore = pathMore!!)
                .subscribeOn(Schedulers.io())
                .doOnNext {
                    pathMore = it.pathMore
                }
                .map { response ->
                    return@map response.images.map { image ->
                        ImageGalleryAdapter.ViewModel().also {
                            it.dataViewType = ImageGalleryAdapter.TYPE_DATA_CONTENTS
                            it.title = image.title
                            it.src = image.src
                            it.link = image.link
                            it.viewId = image.viewId
                        }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    imageGalleryAdapter.addDataAll(it)
                    if (it.isEmpty()) {
                        imageGalleryAdapter.setFooterDisplay(false)
                        imageGalleryAdapter.notifyItemChanged(imageGalleryAdapter.itemCount)
                    }
                }, {
                    LogUtil.e(it)
                    if (it is LoginRequiredException) {
                        startActivitySubject.onNext(StartActivitySubject(null, LoginActivity.intent(context)))
                    }
                })
    }

}
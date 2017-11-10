package ns.me.ns.furaffinity.ui.viewmodel

import android.app.Application
import android.support.v7.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ns.me.ns.furaffinity.datasouce.web.FurAffinityWebApiClient
import ns.me.ns.furaffinity.ui.adapter.OnEndScrollListener
import ns.me.ns.furaffinity.ui.adapter.recycler.ImageGalleryAdapter
import ns.me.ns.furaffinity.util.LogUtil

class MainViewModel(application: Application) : AbstractBaseViewModel(application) {

    val imageGalleryAdapter: ImageGalleryAdapter by lazy {
        ImageGalleryAdapter(application)
    }

    var currentPage: Int = 0

    fun getOnEndScrollListener(): OnEndScrollListener {
        return object : OnEndScrollListener() {
            override fun onEndScroll(recyclerView: RecyclerView, dx: Int, dy: Int) {
                currentPage++
                getBrowse()
            }
        }
    }

    fun getBrowse() {
        val page = currentPage
        FurAffinityWebApiClient.service.getBrowse(page = page)
                .subscribeOn(Schedulers.io())
                .map { response ->
                    return@map response.imageSrcs.map { imageSrc ->
                        ImageGalleryAdapter.ViewModel().also {
                            it.dataViewType = ImageGalleryAdapter.TYPE_DATA_CONTENTS
                            it.description = null
                            it.imageSrc = imageSrc
                        }
                    }
                }
                .map {
                    val result = ArrayList<ImageGalleryAdapter.ViewModel>()
                    val section = ImageGalleryAdapter.ViewModel().also {
                        it.dataViewType = ImageGalleryAdapter.TYPE_DATA_PAGE
                        it.description = "${page}"
                        it.imageSrc = null
                    }
                    result.add(section)
                    result.addAll(it)
                    return@map result
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    imageGalleryAdapter.addDataAll(it)
                }, {
                    LogUtil.e(it)
                })
    }

}
package ns.me.ns.furaffinity.ui.viewmodel

import android.app.Application
import android.support.v7.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ns.me.ns.furaffinity.exception.LoginRequiredException
import ns.me.ns.furaffinity.repository.GalleryRepository
import ns.me.ns.furaffinity.repository.model.local.Gallery
import ns.me.ns.furaffinity.ui.activity.LoginActivity
import ns.me.ns.furaffinity.ui.adapter.AbstractRecyclerViewAdapter
import ns.me.ns.furaffinity.ui.adapter.OnEndScrollListener
import ns.me.ns.furaffinity.ui.adapter.recycler.GalleryAdapter
import ns.me.ns.furaffinity.util.LogUtil
import javax.inject.Inject

class GalleryViewModel @Inject constructor(application: Application) : AbstractBaseViewModel(application) {

    @Inject
    lateinit var galleryRepository: GalleryRepository

    var account: String? = null

    var adapterPage: Int = 1

    val adapterOnItemClickSubject: PublishSubject<AbstractRecyclerViewAdapter.OnClickItem<GalleryAdapter.ContentsViewModel>> = PublishSubject.create()

    val galleryAdapter: GalleryAdapter by lazy {
        GalleryAdapter(application).apply {
            onItemClickPublishSubject.subscribe {
                adapterOnItemClickSubject.onNext(it)
            }
            onRequestReload = {
                getGallery()
            }
            setFooterDisplay(true)
        }
    }

    val onEndScrollListener = object : OnEndScrollListener() {
        override fun onEndScroll(recyclerView: RecyclerView, dx: Int, dy: Int) {
            getGallery()
        }
    }

    private fun getGallery() {
        val account = account ?: return

        val get = if (galleryAdapter.dataCount <= 0) galleryRepository.get(account) else galleryRepository.getMore(account, adapterPage)
        get.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    adapterPage++
                    galleryAdapter.setLoadingError(false)
                    appendAdapterItems(it)
                }, {
                    LogUtil.e(it)
                    galleryAdapter.setLoadingError(true)
                    if (it is LoginRequiredException) {
                        startActivitySubject.onNext(LoginActivity.intent(context))
                        finishActivitySubject.onNext(Unit)
                    }
                })

    }

    private fun appendAdapterItems(responseItems: List<Gallery>) {
        val adapterItems = galleryAdapter.getDataList()
        if (responseItems.isEmpty()) {
            // それ以外:APIの返却がない場合
            // フッタを除去
            galleryAdapter.setFooterDisplay(false)
            return
        }

        // 重複が存在している場合はフィルタを行う
        val filterItems = responseItems.filter {
            // アダプター要素に存在しないものを抽出
            adapterItems.find { adapterItem -> it.viewId == adapterItem.viewId } == null
        }.map {
            GalleryAdapter.ContentsViewModel(it)
        }

        if (responseItems.isNotEmpty() && filterItems.isEmpty()) {
            // フィルタしたデータが空の場合でも、APIからの返却がある場合は、
            // 未取得データを考慮して取得処理を継続する
            // ※ OnEndScrollListenerは要素の数の条件が変わらないとコールされない
            getGallery()
        } else {
            // データが存在する場合は要素を追加
            galleryAdapter.addDataAll(filterItems)
        }

    }


}
package ns.me.ns.furaffinity.ui.viewmodel

import android.app.Application
import android.databinding.ObservableField
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.exception.DisplayUIError
import ns.me.ns.furaffinity.exception.LoginRequiredException
import ns.me.ns.furaffinity.repository.BrowseRepository
import ns.me.ns.furaffinity.repository.model.local.Browse
import ns.me.ns.furaffinity.ui.activity.LoginActivity
import ns.me.ns.furaffinity.ui.adapter.AbstractRecyclerViewAdapter
import ns.me.ns.furaffinity.ui.adapter.OnEndScrollListener
import ns.me.ns.furaffinity.ui.adapter.recycler.BrowseAdapter
import ns.me.ns.furaffinity.util.LogUtil
import javax.inject.Inject

class BrowseViewModel @Inject constructor(application: Application) : AbstractBaseViewModel(application) {

    @Inject
    lateinit var browseRepository: BrowseRepository

    var adapterPage: Int = 1

    val adapterOnItemClickSubject: PublishSubject<AbstractRecyclerViewAdapter.OnClickItem<BrowseAdapter.ContentsViewModel>> = PublishSubject.create()

    val browseAdapter: BrowseAdapter by lazy {
        BrowseAdapter(application).apply {
            onItemClickPublishSubject.subscribe {
                adapterOnItemClickSubject.onNext(it)
            }
            onRequestReload = {
                getBrowse()
            }
            setFooterDisplay(true)
        }
    }

    val onEndScrollListener = object : OnEndScrollListener() {
        override fun onEndScroll(recyclerView: RecyclerView, dx: Int, dy: Int) {
            getBrowse()
        }
    }

    val onRefreshListener = SwipeRefreshLayout.OnRefreshListener { refreshBrowse() }

    val refreshing = ObservableField<Boolean>().apply { set(false) }

    private fun getBrowse() {
        if (refreshing.get() == true) return

        val get = if (browseAdapter.dataCount <= 0) browseRepository.get() else browseRepository.getMore(adapterPage)
        get.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    adapterPage++
                    browseAdapter.setLoadingError(false)
                    appendAdapterItems(it)
                }, {
                    LogUtil.e(it)
                    browseAdapter.setLoadingError(true)
                    if (it is LoginRequiredException) {
                        startActivitySubject.onNext(LoginActivity.intent(context))
                        finishActivitySubject.onNext(Unit)
                    }
                })

    }

    private fun appendAdapterItems(responseItems: List<Browse>) {
        val adapterItems = browseAdapter.getDataList()
        if (responseItems.isEmpty()) {
            // それ以外:APIの返却がない場合
            // フッタを除去
            browseAdapter.setFooterDisplay(false)
            return
        }

        // 重複が存在している場合はフィルタを行う
        val filterItems = responseItems.filter {
            // アダプター要素に存在しないものを抽出
            adapterItems.find { adapterItem -> it.viewId == adapterItem.viewId } == null
        }.map {
            BrowseAdapter.ContentsViewModel(it)
        }

        if (responseItems.isNotEmpty() && filterItems.isEmpty()) {
            // フィルタしたデータが空の場合でも、APIからの返却がある場合は、
            // 未取得データを考慮して取得処理を継続する
            // ※ OnEndScrollListenerは要素の数の条件が変わらないとコールされない
            getBrowse()
        } else {
            // データが存在する場合は要素を追加
            browseAdapter.addDataAll(filterItems)
        }

    }

    private fun refreshBrowse() {
        refreshing.set(true)
        browseAdapter.setFooterDisplay(false)
        adapterPage = 0
        browseRepository.refresh()
                .map {
                    return@map it.map {
                        BrowseAdapter.ContentsViewModel(it)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    browseAdapter.clear()
                }
                .subscribe({
                    browseAdapter.setLoadingError(false)
                    refreshing.set(false)
                    if (it.isNotEmpty()) {
                        browseAdapter.addDataAll(it)
                        browseAdapter.setFooterDisplay(true)
                    } else {
                        browseAdapter.setFooterDisplay(false)
                    }
                }, {
                    LogUtil.e(it)
                    refreshing.set(false)
                    displayUIErrorSubject.onNext(DisplayUIError(R.string.error_network))
                })
    }

}
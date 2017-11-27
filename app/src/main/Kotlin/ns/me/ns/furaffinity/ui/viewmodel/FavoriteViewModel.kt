package ns.me.ns.furaffinity.ui.viewmodel

import android.app.Application
import android.databinding.ObservableField
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ns.me.ns.furaffinity.repository.FavoriteRepository
import ns.me.ns.furaffinity.ui.adapter.AbstractRecyclerViewAdapter
import ns.me.ns.furaffinity.ui.adapter.recycler.FavoriteAdapter
import ns.me.ns.furaffinity.util.LogUtil
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(application: Application) : AbstractBaseViewModel(application) {

    @Inject
    lateinit var favoriteRepository: FavoriteRepository

    var isEmptyFavorite = ObservableField<Boolean>()

    val adapterOnItemClickSubject: PublishSubject<AbstractRecyclerViewAdapter.OnClickItem<FavoriteAdapter.ContentsViewModel>> = PublishSubject.create()

    val favoriteAdapter: FavoriteAdapter by lazy {
        FavoriteAdapter(application).apply {
            onItemClickPublishSubject.subscribe {
                adapterOnItemClickSubject.onNext(it)
            }
            setFooterDisplay(false)
        }
    }

    fun refreshAdapter() {
        favoriteRepository.getLocal()
                .subscribeOn(Schedulers.io())
                .map {
                    return@map it.map { FavoriteAdapter.ContentsViewModel(it) }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    favoriteAdapter.clear()
                    favoriteAdapter.addDataAll(it)
                    isEmptyFavorite.set(it.isEmpty())
                }, {
                    LogUtil.e(it)
                })

    }


}
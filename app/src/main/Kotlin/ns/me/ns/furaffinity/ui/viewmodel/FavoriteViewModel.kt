package ns.me.ns.furaffinity.ui.viewmodel

import android.app.Application
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ns.me.ns.furaffinity.ds.local.dao.FavoriteDao
import ns.me.ns.furaffinity.ui.adapter.recycler.FavoriteAdapter
import ns.me.ns.furaffinity.util.LogUtil
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(application: Application) : AbstractBaseViewModel(application) {

    @Inject
    lateinit var favoriteDao: FavoriteDao

    val fullViewSubject: PublishSubject<Pair<View?, FavoriteAdapter.ContentsViewModel>> = PublishSubject.create()

    val favoriteAdapter: FavoriteAdapter by lazy {
        FavoriteAdapter(application).apply {
            onItemClick = { _, data, view ->
                fullViewSubject.onNext(Pair(view, data))
            }
            setFooterDisplay(false)
            favoriteDao.all()
                    .subscribeOn(Schedulers.io())
                    .map {
                        return@map it.map { FavoriteAdapter.ContentsViewModel(it) }
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        addDataAll(it)
                    }, {
                        LogUtil.e(it)
                    })
        }
    }


}
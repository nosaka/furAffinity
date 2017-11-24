package ns.me.ns.furaffinity.repository

import io.reactivex.Completable
import io.reactivex.Single
import ns.me.ns.furaffinity.ds.local.dao.FavoriteDao
import ns.me.ns.furaffinity.ds.local.model.Favorite
import javax.inject.Inject

/**
 * [Favorite] Repository
 */
class FavoriteRepository @Inject constructor(private val favoriteDao: FavoriteDao) {

    fun find(viewId: Int): Single<Favorite> = Single.fromCallable { return@fromCallable favoriteDao.find(viewId) }

    fun get(): Single<List<Favorite>> = Single.just(favoriteDao.all())

    fun save(favorite: Favorite): Completable = Completable.fromAction {
        favoriteDao.insert(favorite)
    }

    fun remove(viewId: Int): Completable = Completable.fromAction {
        favoriteDao.delete(viewId)
    }


}
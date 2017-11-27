package ns.me.ns.furaffinity.repository

import io.reactivex.Completable
import io.reactivex.Single
import ns.me.ns.furaffinity.ds.dao.FavoriteDao
import ns.me.ns.furaffinity.repository.model.local.Favorite
import javax.inject.Inject

/**
 * [Favorite] Repository
 */
class FavoriteRepository @Inject constructor(private val favoriteDao: FavoriteDao) {

    fun find(viewId: Int): Single<Favorite> = Single.fromCallable { return@fromCallable favoriteDao.find(viewId) }

    fun getLocal(): Single<List<Favorite>> = Single.just(favoriteDao.all())

    fun save(favorite: Favorite): Completable = Completable.fromAction {
        favoriteDao.insert(favorite)
    }

    fun remove(viewId: Int): Completable = Completable.fromAction {
        favoriteDao.delete(viewId)
    }


}
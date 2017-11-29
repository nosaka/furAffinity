package ns.me.ns.furaffinity.repository

import android.graphics.Bitmap
import io.reactivex.Completable
import io.reactivex.Single
import ns.me.ns.furaffinity.ds.dao.FavoriteDao
import ns.me.ns.furaffinity.repository.model.ViewInterface
import ns.me.ns.furaffinity.repository.model.local.Favorite
import ns.me.ns.furaffinity.util.BitmapUtil
import javax.inject.Inject

/**
 * [Favorite] Repository
 */
class FavoriteRepository @Inject constructor(private val favoriteDao: FavoriteDao) {

    fun find(viewId: Int): Single<Favorite> = Single.fromCallable { return@fromCallable favoriteDao.find(viewId) }

    fun getLocal(): Single<List<Favorite>> = Single.just(favoriteDao.all())

    fun save(value: ViewInterface, bitmap: Bitmap?): Completable = Completable.fromAction {
        convert(value, bitmap)?.let {
            favoriteDao.insert(it)
        }
    }

    fun remove(viewId: Int): Completable = Completable.fromAction {
        favoriteDao.delete(viewId)
    }

    private fun convert(value: ViewInterface, bitmap: Bitmap?): Favorite? {
        val entity = Favorite()
        entity.viewId = value.viewId
        entity.name = value.name
        entity.src = value.imageElement.get()?.src
        entity.alt = value.imageElement.get()?.alt
        entity.account = value.userElement.get()?.account
        bitmap?.let {
            entity.imageData = BitmapUtil.decodeByteArray(it)
        }
        return entity
    }

}
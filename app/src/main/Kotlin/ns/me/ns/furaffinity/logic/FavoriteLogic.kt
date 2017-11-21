package ns.me.ns.furaffinity.logic

import android.graphics.Bitmap
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import ns.me.ns.furaffinity.ds.local.dao.FavoriteDao
import ns.me.ns.furaffinity.ds.local.model.Favorite
import ns.me.ns.furaffinity.util.BitmapUtil
import javax.inject.Inject

/**
 *
 */
class FavoriteLogic @Inject constructor(private val favoriteDao: FavoriteDao) {

    fun find(viewId: Int): Maybe<Favorite> = favoriteDao.find(viewId)

    fun isFavorite(viewId: Int): Single<Boolean> = favoriteDao.find(viewId).toSingle().map { true }.onErrorReturn { false }

    fun save(viewId: Int,
             src: String?,
             alt: String?,
             bitmap: Bitmap? = null): Completable = Completable.fromAction {
        val data = Favorite()
        data.viewId = viewId
        data.src = src
        data.alt = alt
        bitmap?.let {
            data.imageData = BitmapUtil.decodeByteArray(it)
        }

        favoriteDao.insert(data)
    }

    fun remove(viewId: Int): Completable = Completable.fromAction {
        favoriteDao.delete(viewId)
    }


}
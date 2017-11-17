package ns.me.ns.furaffinity.pacakge

import android.graphics.Bitmap
import io.reactivex.Completable
import io.reactivex.Maybe
import ns.me.ns.furaffinity.ds.local.dao.FavoriteDao
import ns.me.ns.furaffinity.ds.local.model.Favorite
import ns.me.ns.furaffinity.util.BitmapUtil
import javax.inject.Inject

/**
 *
 */
class FavoriteLogic @Inject constructor(private val favoriteDao: FavoriteDao) {

    fun find(viewId: Int): Maybe<Favorite> = favoriteDao.find(viewId)

    fun save(viewId: Int,
             src: String?,
             alt: String?,
             bitmap: Bitmap? = null) = Completable.fromAction {
        val data = Favorite()
        data.viewId = viewId
        data.src = src
        data.alt = alt
        bitmap?.let {
            data.imageData = BitmapUtil.decodeByteArray(it)
        }

        favoriteDao.insert(data)
    }


}
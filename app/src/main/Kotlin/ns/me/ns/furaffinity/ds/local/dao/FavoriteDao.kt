package ns.me.ns.furaffinity.ds.local.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Maybe
import ns.me.ns.furaffinity.ds.local.model.Favorite

/**
 *
 */
@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: Favorite)

    @Update
    fun update(value: Favorite)

    @Delete
    fun delete(value: Favorite)

    @Query("DELETE FROM Favorite")
    fun deleteAll()

    @Query("SELECT * FROM Favorite WHERE VIEW_ID = :viewId")
    fun find(viewId: Int): Maybe<Favorite>

    @Query("SELECT * FROM Favorite")
    fun all(): Flowable<List<Favorite>>

}
package ns.me.ns.furaffinity.ds.dao

import android.arch.persistence.room.*
import ns.me.ns.furaffinity.repository.model.local.Favorite

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

    @Query("DELETE FROM Favorite WHERE VIEW_ID = :viewId")
    fun delete(viewId: Int)

    @Query("DELETE FROM Favorite")
    fun deleteAll()

    @Query("SELECT * FROM Favorite WHERE VIEW_ID = :viewId")
    fun find(viewId: Int): Favorite?

    @Query("SELECT * FROM Favorite")
    fun all(): List<Favorite>

}
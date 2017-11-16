package ns.me.ns.furaffinity.datasouce.local.dao

import android.arch.persistence.room.*
import ns.me.ns.furaffinity.datasouce.local.model.Favorite

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

    @Query("SELECT * FROM Favorite")
    fun all(): List<Favorite>

}
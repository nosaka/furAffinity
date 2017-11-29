package ns.me.ns.furaffinity.ds.dao

import android.arch.persistence.room.*
import ns.me.ns.furaffinity.repository.model.local.Gallery

/**
 *
 */
@Dao
interface GalleryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: Gallery)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg value: Gallery)

    @Update
    fun update(value: Gallery)

    @Delete
    fun delete(value: Gallery)

    @Query("DELETE FROM Gallery WHERE VIEW_ID = :viewId")
    fun delete(viewId: Int)

    @Query("DELETE FROM Gallery")
    fun deleteAll()

    @Query("SELECT * FROM Gallery WHERE VIEW_ID = :viewId")
    fun find(viewId: Int): Gallery?

    @Query("SELECT * FROM Gallery ORDER BY VIEW_ID DESC")
    fun all(): List<Gallery>

    @Query("SELECT * FROM Gallery WHERE ACCOUNT = :account ORDER BY VIEW_ID DESC")
    fun all(account: String): List<Gallery>


}
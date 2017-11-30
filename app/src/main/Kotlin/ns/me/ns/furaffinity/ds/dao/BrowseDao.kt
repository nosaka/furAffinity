package ns.me.ns.furaffinity.ds.dao

import android.arch.persistence.room.*
import ns.me.ns.furaffinity.repository.model.local.Browse

/**
 *
 */
@Dao
interface BrowseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: Browse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg value: Browse)

    @Update
    fun update(value: Browse)

    @Delete
    fun delete(value: Browse)

    @Query("DELETE FROM Browse WHERE VIEW_ID = :viewId")
    fun delete(viewId: Int)

    @Query("DELETE FROM Browse")
    fun deleteAll()

    @Query("SELECT * FROM Browse WHERE VIEW_ID = :viewId")
    fun find(viewId: Int): Browse?

    @Query("SELECT * FROM Browse ORDER BY VIEW_ID DESC")
    fun all(): List<Browse>

    @Query("SELECT * FROM Browse WHERE VIEW_ID < :viewId ORDER BY VIEW_ID DESC")
    fun allThanViewId(viewId: Int): List<Browse>


}
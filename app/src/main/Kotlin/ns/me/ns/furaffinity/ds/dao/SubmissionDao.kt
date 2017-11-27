package ns.me.ns.furaffinity.ds.dao

import android.arch.persistence.room.*
import ns.me.ns.furaffinity.repository.model.local.Submission

/**
 *
 */
@Dao
interface SubmissionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: Submission)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg value: Submission)

    @Update
    fun update(value: Submission)

    @Delete
    fun delete(value: Submission)

    @Query("DELETE FROM Submission WHERE VIEW_ID = :viewId")
    fun delete(viewId: Int)

    @Query("DELETE FROM Submission")
    fun deleteAll()

    @Query("SELECT * FROM Submission WHERE VIEW_ID = :viewId")
    fun find(viewId: Int): Submission?

    @Query("SELECT * FROM Submission ORDER BY VIEW_ID DESC")
    fun all(): List<Submission>

    @Query("SELECT * FROM Submission WHERE VIEW_ID < :viewId ORDER BY VIEW_ID DESC")
    fun allThanViewId(viewId: Int): List<Submission>


}
package ns.me.ns.furaffinity.ds.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import ns.me.ns.furaffinity.repository.model.local.Browse

/**
 *
 */
@Database(entities = arrayOf(Browse::class), version = 1, exportSchema = false)
abstract class MemoryDatabase : RoomDatabase() {

    companion object {

        private var instance: MemoryDatabase? = null

        fun getDatabase(context: Context): MemoryDatabase {
            if (instance == null) {
                instance = Room.inMemoryDatabaseBuilder(context.applicationContext, MemoryDatabase::class.java)
                        .allowMainThreadQueries()
                        .build()
            }
            return instance!!
        }

        fun destroyInstance() {
            instance = null
        }

    }

    abstract fun browseDao(): BrowseDao
}
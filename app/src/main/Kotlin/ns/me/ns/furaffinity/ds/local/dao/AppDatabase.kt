package ns.me.ns.furaffinity.ds.local.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import ns.me.ns.furaffinity.ds.local.model.Favorite

/**
 *
 */
@Database(entities = arrayOf(Favorite::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app.db")
                        .allowMainThreadQueries()
                        .build()
            }
            return instance!!
        }

        fun destroyInstance() {
            instance = null
        }

    }

    abstract fun favoriteDao(): FavoriteDao
}
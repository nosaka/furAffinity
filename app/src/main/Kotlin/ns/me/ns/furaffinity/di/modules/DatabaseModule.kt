package ns.me.ns.furaffinity.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ns.me.ns.furaffinity.datasouce.local.dao.AppDatabase
import ns.me.ns.furaffinity.datasouce.local.dao.FavoriteDao
import javax.inject.Singleton

/**
 * Database Provider
 */
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase = AppDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideSubmissionDao(database: AppDatabase): FavoriteDao = database.favoriteDao()

}
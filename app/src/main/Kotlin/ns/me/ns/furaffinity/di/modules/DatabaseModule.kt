package ns.me.ns.furaffinity.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ns.me.ns.furaffinity.ds.dao.AppDatabase
import ns.me.ns.furaffinity.ds.dao.FavoriteDao
import ns.me.ns.furaffinity.ds.dao.GalleryDao
import ns.me.ns.furaffinity.ds.dao.SubmissionDao
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
    fun provideFavoriteDao(database: AppDatabase): FavoriteDao = database.favoriteDao()

    @Provides
    @Singleton
    fun provideSubmissionDao(database: AppDatabase): SubmissionDao = database.submissionDao()

    @Provides
    @Singleton
    fun provideGalleryDao(database: AppDatabase): GalleryDao = database.galleryDao()

}
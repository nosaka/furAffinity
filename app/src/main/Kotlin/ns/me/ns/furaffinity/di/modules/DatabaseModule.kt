package ns.me.ns.furaffinity.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ns.me.ns.furaffinity.ds.dao.*
import javax.inject.Singleton

/**
 * Database Provider
 */
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDiscDatabase(context: Context): DiscDatabase = DiscDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideMemoryDatabase(context: Context): MemoryDatabase = MemoryDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideFavoriteDao(database: DiscDatabase): FavoriteDao = database.favoriteDao()

    @Provides
    @Singleton
    fun provideSubmissionDao(database: DiscDatabase): SubmissionDao = database.submissionDao()

    @Provides
    @Singleton
    fun provideGalleryDao(database: DiscDatabase): GalleryDao = database.galleryDao()

    @Provides
    @Singleton
    fun provideBrowseDao(database: MemoryDatabase): BrowseDao = database.browseDao()

}
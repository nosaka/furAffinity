package ns.me.ns.furaffinity.di.modules

import dagger.Module
import dagger.Provides
import ns.me.ns.furaffinity.ds.dao.FavoriteDao
import ns.me.ns.furaffinity.ds.dao.SubmissionDao
import ns.me.ns.furaffinity.ds.webapi.AppWebApiService
import ns.me.ns.furaffinity.repository.FavoriteRepository
import ns.me.ns.furaffinity.repository.SubmissionRepository
import javax.inject.Singleton

/**
 * Database Provider
 */
@Module(includes = arrayOf(DatabaseModule::class))
class RepositoryModule {

    @Provides
    @Singleton
    fun provideSubmissionRepository(service: AppWebApiService, submissionDao: SubmissionDao): SubmissionRepository = SubmissionRepository(service, submissionDao)

    @Provides
    @Singleton
    fun provideFavoriteRepository(favoriteDao: FavoriteDao): FavoriteRepository = FavoriteRepository(favoriteDao)

}
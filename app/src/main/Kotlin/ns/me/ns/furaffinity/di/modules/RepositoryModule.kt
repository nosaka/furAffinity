package ns.me.ns.furaffinity.di.modules

import dagger.Module
import dagger.Provides
import ns.me.ns.furaffinity.ds.local.dao.SubmissionDao
import ns.me.ns.furaffinity.ds.remote.AppWebApiService
import ns.me.ns.furaffinity.repository.SubmissionRepository
import javax.inject.Singleton

/**
 * Database Provider
 */
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideSubmissionRepository(service:AppWebApiService, submissionDao: SubmissionDao): SubmissionRepository = SubmissionRepository(service, submissionDao)

}
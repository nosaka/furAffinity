package ns.me.ns.furaffinity.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ns.me.ns.furaffinity.ds.webapi.AppWebApiClient
import ns.me.ns.furaffinity.ds.webapi.AppWebApiService
import javax.inject.Singleton

/**
 * Web Provider
 */
@Module
class WebModule {

    @Provides
    @Singleton
    fun provideAppWebApiService(context: Context): AppWebApiService = AppWebApiClient.service(context)

}
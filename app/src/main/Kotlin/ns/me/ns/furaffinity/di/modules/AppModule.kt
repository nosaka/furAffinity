package ns.me.ns.furaffinity.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Provider for App
 */
@Module(includes = arrayOf(LogicModule::class, WebModule::class, DatabaseModule::class, ViewModule::class))
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = this.application

    @Provides
    @Singleton
    fun provideContext(): Context = this.application

}

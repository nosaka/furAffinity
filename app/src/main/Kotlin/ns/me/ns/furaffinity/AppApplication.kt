package ns.me.ns.furaffinity

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import ns.me.ns.furaffinity.di.AppInjector
import ns.me.ns.furaffinity.di.compornent.DaggerAppComponent
import ns.me.ns.furaffinity.di.modules.AppModule

/**
 * [Application]
 */
class AppApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
                .application(this)
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        AppInjector.applyAutoInjector(this)
    }

}
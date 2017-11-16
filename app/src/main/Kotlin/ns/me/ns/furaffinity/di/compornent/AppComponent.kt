package ns.me.ns.furaffinity.di.compornent

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ns.me.ns.furaffinity.AppApplication
import ns.me.ns.furaffinity.di.modules.AppModule
import javax.inject.Singleton

/**
 * Component for App
 */
@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class)
)
interface AppComponent : AndroidInjector<AppApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: AppApplication): Builder
        fun appModule(module: AppModule): Builder
        fun build(): AppComponent
    }

    override fun inject(app: AppApplication)
}
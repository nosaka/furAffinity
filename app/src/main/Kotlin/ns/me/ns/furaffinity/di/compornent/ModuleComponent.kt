package ns.me.ns.furaffinity.di.compornent

import dagger.Component
import ns.me.ns.furaffinity.di.modules.ContextModule
import ns.me.ns.furaffinity.di.modules.RepositoryModule
import ns.me.ns.furaffinity.di.modules.WebModule
import ns.me.ns.furaffinity.ui.adapter.pager.FullViewPagerAdapter
import javax.inject.Singleton

/**
 * Component for App
 */
@Singleton
@Component(modules = arrayOf(ContextModule::class, WebModule::class, RepositoryModule::class)
)
interface ModuleComponent {

    @Component.Builder
    interface Builder {
        fun contextModule(module: ContextModule): Builder
        fun build(): ModuleComponent
    }

    fun inject(adapter: FullViewPagerAdapter)
}
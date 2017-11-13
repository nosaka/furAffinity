package ns.me.ns.furaffinity.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ns.me.ns.furaffinity.ui.activity.FullViewActivity
import ns.me.ns.furaffinity.ui.activity.LoginActivity
import ns.me.ns.furaffinity.ui.activity.MainActivity

/**
 * View Binder
 */
@Module
abstract class ViewModule {

    @ContributesAndroidInjector(modules = arrayOf(ViewBindModelModule::class))
    internal abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = arrayOf(ViewBindModelModule::class))
    internal abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = arrayOf(ViewBindModelModule::class))
    internal abstract fun contributeFullViewActivity(): FullViewActivity


}
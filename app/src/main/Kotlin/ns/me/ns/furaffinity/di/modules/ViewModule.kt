package ns.me.ns.furaffinity.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ns.me.ns.furaffinity.ui.activity.FullViewActivity
import ns.me.ns.furaffinity.ui.activity.GalleryActivity
import ns.me.ns.furaffinity.ui.activity.LoginActivity
import ns.me.ns.furaffinity.ui.activity.MainActivity
import ns.me.ns.furaffinity.ui.fragment.FavoriteFragment
import ns.me.ns.furaffinity.ui.fragment.SubmissionsFragment

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

    @ContributesAndroidInjector(modules = arrayOf(ViewBindModelModule::class))
    internal abstract fun contributeGalleryActivity(): GalleryActivity

    @ContributesAndroidInjector(modules = arrayOf(ViewBindModelModule::class))
    internal abstract fun contributeSubmissionsFragment(): SubmissionsFragment

    @ContributesAndroidInjector(modules = arrayOf(ViewBindModelModule::class))
    internal abstract fun contributeFavoriteFragment(): FavoriteFragment

}
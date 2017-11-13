package ns.me.ns.furaffinity.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ns.me.ns.furaffinity.di.AppViewModelFactory
import ns.me.ns.furaffinity.di.ViewModelKey
import ns.me.ns.furaffinity.ui.viewmodel.FullViewViewModel
import ns.me.ns.furaffinity.ui.viewmodel.LoginViewModel
import ns.me.ns.furaffinity.ui.viewmodel.MainViewModel

/**
 *
 */
@Module
internal abstract class ViewBindModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FullViewViewModel::class)
    abstract fun bindFullViewViewModel(viewModel: FullViewViewModel): ViewModel



}


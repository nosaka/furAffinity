package ns.me.ns.furaffinity.ui.activity

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ns.me.ns.furaffinity.ui.viewmodel.AbstractBaseViewModel

/**
 * 抽象基底Activity
 */
abstract class AbstractBaseActivity<out ViewModel : AbstractBaseViewModel> : AppCompatActivity() {

    private val lifecycleRegistry = LifecycleRegistry(this)

    protected val viewModelFactory:ViewModelProvider.Factory by lazy {
        ViewModelProviders.DefaultFactory(application)
    }

    abstract val viewModel: ViewModel

    override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreate()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        viewModel.onPostCreate()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        viewModel.onConfigurationChanged()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onStop() {
        viewModel.onStop()
        super.onStop()
    }

    override fun onPause() {
        viewModel.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        super.onDestroy()
    }

}
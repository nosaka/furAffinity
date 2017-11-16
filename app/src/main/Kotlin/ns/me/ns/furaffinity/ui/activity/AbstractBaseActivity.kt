package ns.me.ns.furaffinity.ui.activity

import android.arch.lifecycle.LifecycleRegistry
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import ns.me.ns.furaffinity.ui.viewmodel.AbstractBaseViewModel


/**
 * 抽象基底Activity
 */
abstract class AbstractBaseActivity<out ViewModel : AbstractBaseViewModel> : AppCompatActivity() {

    companion object {
        const val REQUEST_PERMISSION = 0x01
    }

    private val lifecycleRegistry = LifecycleRegistry(this)

    abstract val viewModel: ViewModel

    override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreate()
        viewModel.systemUISubject.subscribe {
            val visible = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0
            if (visible) {
                hideSystemUI()
            } else {
                showSystemUI()
            }
        }
        viewModel.finishActivitySubject.subscribe {
            if (!isFinishing) {
                finish()
            }

        }
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

    protected open fun hideSystemUI() {
        window.decorView.clearFocus()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    protected open fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }


}
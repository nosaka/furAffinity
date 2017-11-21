package ns.me.ns.furaffinity.ui.activity

import android.arch.lifecycle.LifecycleRegistry
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import ns.me.ns.furaffinity.ui.Disposer
import ns.me.ns.furaffinity.ui.viewmodel.AbstractBaseViewModel


/**
 * 抽象基底Activity
 */
abstract class AbstractBaseActivity<out ViewModel : AbstractBaseViewModel> : AppCompatActivity() {

    companion object {
        const val REQUEST_PERMISSION = 0x01
    }

    private val lifecycleRegistry = LifecycleRegistry(this)

    protected val disposer: Disposer = Disposer(lifecycle)

    abstract val viewModel: ViewModel

    override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.startActivitySubject.subscribe {
            startActivity(it)
        }.also { disposer.add(it) }

        viewModel.systemUISubject.subscribe {
            val visible = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0
            if (visible) {
                hideSystemUI()
            } else {
                showSystemUI()
            }
        }.also { disposer.add(it) }

        viewModel.finishActivitySubject.subscribe {
            if (!isFinishing) {
                finish()
            }

        }.also { disposer.add(it) }
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
package ns.me.ns.furaffinity.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import ns.me.ns.furaffinity.ui.Disposer
import ns.me.ns.furaffinity.ui.viewmodel.AbstractBaseViewModel

/**
 * 抽象基底Fragment
 */
abstract class AbstractBaseFragment<out ViewModel : AbstractBaseViewModel> : Fragment() {

    val disposer: Disposer = Disposer(lifecycle)

    abstract val viewModel: ViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.startActivitySubject.subscribe {
            startActivity(it)
        }

        viewModel.systemUISubject.subscribe {
            val decorView = activity?.window?.decorView ?: return@subscribe
            val visible = decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0
            if (visible) {
                hideSystemUI()
            } else {
                showSystemUI()
            }
        }

        viewModel.finishActivitySubject.subscribe {
            if (isAdded && activity?.isFinishing == false) {
                activity?.finish()
            }
        }
    }

    protected open fun hideSystemUI() {
        activity?.window?.decorView?.clearFocus()
        activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    protected open fun showSystemUI() {
        activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

}
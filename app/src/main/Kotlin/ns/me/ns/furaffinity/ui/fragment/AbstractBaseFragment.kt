package ns.me.ns.furaffinity.ui.fragment

import android.arch.lifecycle.LifecycleRegistry
import android.support.v4.app.Fragment
import ns.me.ns.furaffinity.ui.viewmodel.AbstractBaseViewModel

/**
 * 抽象基底Fragment
 */
abstract class AbstractBaseFragment<out ViewModel : AbstractBaseViewModel> : Fragment() {

    private val lifecycleRegistry = LifecycleRegistry(this)

    abstract val viewModel: ViewModel

    override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

}
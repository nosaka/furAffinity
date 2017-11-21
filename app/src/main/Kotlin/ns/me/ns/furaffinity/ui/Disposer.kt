package ns.me.ns.furaffinity.ui

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.Disposable

/**
 *
 */
class Disposer(val lifecycle: Lifecycle) : LifecycleObserver {

    private val disposeTiming: Lifecycle.Event = Lifecycle.Event.ON_DESTROY

    init {
        lifecycle.addObserver(this)
    }

    private val disposables: MutableList<Disposable> = mutableListOf()

    fun add(disposable: Disposable) = disposables.add(disposable)

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disposeIfTiming(owner: LifecycleOwner) {
        disposables.forEach { it.dispose() }
        disposables.clear()

    }
}
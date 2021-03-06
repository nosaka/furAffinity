package ns.me.ns.furaffinity.ui.viewmodel

/**
 * Lifecycle for ViewModel
 */
interface ViewModelLifecycle {

    fun onCreate() {}

    fun onPostCreate() {}

    fun onConfigurationChanged() {}

    fun onResume() {}

    fun onStart() {}

    fun onStop() {}

    fun onPause() {}

    fun onDestroy() {}
}
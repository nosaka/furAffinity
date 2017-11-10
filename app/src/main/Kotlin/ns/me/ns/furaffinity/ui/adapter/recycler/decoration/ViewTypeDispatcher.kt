package ns.me.ns.furaffinity.ui.adapter.recycler.decoration

/**
 * ViewTypeDispatcher
 */
interface ViewTypeDispatcher {
    fun isFooter(position: Int): Boolean
    fun isHeader(position: Int): Boolean
}
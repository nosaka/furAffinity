package ns.me.ns.furaffinity.ui.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 *
 */
abstract class OnEndScrollListener : RecyclerView.OnScrollListener() {

    var firstVisibleItem: Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    private var previousTotal = 0
    private var loading = true

    final override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        visibleItemCount = recyclerView.childCount
        (recyclerView.layoutManager as? LinearLayoutManager)?.let {
            totalItemCount = it.itemCount
            firstVisibleItem = it.findFirstVisibleItemPosition()
        }
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount;
            }
        }

        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem)) {
            loading = true
            onEndScroll(recyclerView, dx, dy)
        }
    }

    abstract fun onEndScroll(recyclerView: RecyclerView, dx: Int, dy: Int)

}
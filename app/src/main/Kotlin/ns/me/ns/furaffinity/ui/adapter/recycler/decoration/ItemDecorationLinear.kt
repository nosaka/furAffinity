package ns.me.ns.furaffinity.ui.adapter.recycler.decoration

import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * RecyclerView Item Decoration For Linear
 */
class ItemDecorationLinear(private val orientation: Int, dividerSize: Float) : RecyclerView.ItemDecoration() {

    private var padding = dividerSize.toInt()

    private var paddingHalf = padding / 2

    init {
        if (orientation != LinearLayoutManager.HORIZONTAL && orientation != LinearLayoutManager.VERTICAL) {
            throw IllegalArgumentException("invalid orientation")
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {

        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition

        if (orientation == LinearLayoutManager.VERTICAL) {
            drawVertical(outRect, itemPosition)
        } else {
            val isLast = itemPosition == parent.childCount - 1
            drawHorizontal(outRect, itemPosition, isLast)
        }
    }

    private fun drawVertical(outRect: Rect, itemPosition: Int) {
        // 上
        if (itemPosition <= 0) {
            outRect.top = 0
        } else {
            outRect.top = padding
        }
        // 下
        outRect.bottom = 0
        // 左右
        outRect.left = paddingHalf
        outRect.right = paddingHalf
    }

    private fun drawHorizontal(outRect: Rect, itemPosition: Int, isLast: Boolean) {
        // 上下
        outRect.top = padding
        outRect.bottom = padding
        // 左右
        if (itemPosition <= 0) {
            outRect.left = 0
        } else {
            outRect.left = paddingHalf
        }
        if (isLast) {
            outRect.right = 0
        } else {
            outRect.right = paddingHalf
        }
    }

}
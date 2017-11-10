package ns.me.ns.furaffinity.ui.adapter.recycler.decoration

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


/**
 * RecyclerView Item Decoration For Grid
 */
class ItemDecorationGrid(private val gridSize: Int, dividerSize: Float) : RecyclerView.ItemDecoration() {

    private var padding = dividerSize.toInt()

    private var paddingHalf = padding / 2

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {

        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        (parent.adapter as? ViewTypeDispatcher)?.let {
            if (it.isFooter(itemPosition) || it.isHeader(itemPosition)) {
                return
            }
        }

        // 下
        outRect.bottom = 0
        // 上
        if (itemPosition < gridSize) {
            outRect.top = 0
        } else {
            outRect.top = padding
        }
        // 左右
        if (itemPosition % gridSize == 0) {
            // n列の一番左
            // 例) 1列の(1, 1)座標の場合：0(=itemPosition) % 1(=gridSize) == 0
            // 例) 4列の(2, 1)座標の場合：4(=itemPosition) % 4(=gridSize) == 0
            outRect.left = 0
            outRect.right = paddingHalf
            return
        }
        if ((itemPosition + 1) % gridSize == 0) {
            // n列の一番右
            // 例) 1列の(1, 1)座標の場合：(0(=itemPosition) + 1) % 1(=gridSize) == 0
            // 例) 4列の(2, 4)座標の場合：(7(=itemPosition) + 1) % 4(=gridSize) == 0
            outRect.right = 0
            outRect.left = paddingHalf
            return
        }

        outRect.left = paddingHalf
        outRect.right = paddingHalf


    }

}
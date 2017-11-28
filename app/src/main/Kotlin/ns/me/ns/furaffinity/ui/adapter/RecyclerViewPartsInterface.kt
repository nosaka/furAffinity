package ns.me.ns.furaffinity.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * [RecyclerView]パーツインターフェース
 */
interface RecyclerViewPartsInterface {

    /**
     * ヘッダ表示是非
     */
    var displayHeader: Boolean

    /**
     * フッタ表示是非
     */
    var displayFooter: Boolean


    fun isFooter(position: Int): Boolean

    fun isHeader(position: Int): Boolean

    fun getPosition(itemView: View): Int =
            (itemView.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition

}
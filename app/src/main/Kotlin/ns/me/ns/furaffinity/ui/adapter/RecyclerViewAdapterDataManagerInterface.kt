package ns.me.ns.furaffinity.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * [RecyclerView.Adapter]データ管理インターフェース
 */
interface RecyclerViewAdapterDataManagerInterface<Data> {

    /**
     * データ追加
     *
     * @param position 位置
     * @return [Data]
     */
    fun getData(position: Int): Data?

    /**
     * データ追加
     *
     * @param data [Data]
     */
    fun addData(data: Data)

    /**
     * データ追加
     *
     * @param dataList [Data]リスト
     */
    fun addDataAll(dataList: Collection<Data>)

    /**
     * データ追加
     *
     * @param dataList [Data]配列
     */
    fun addDataAll(vararg dataList: Data)

    /**
     * データ初期化
     */
    fun clear()

    /**
     * データ件数取得
     *
     * @return データ件数
     */
    val dataCount: Int

    /**
     * データ取得
     *
     * @param itemView 要素[View]
     * @return [Data]
     */
    fun getData(itemView: View): Data?
}

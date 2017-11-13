package ns.me.ns.furaffinity.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ns.me.ns.furaffinity.ui.adapter.recycler.decoration.ViewTypeDispatcher
import java.util.*

/**
 * 抽象RecyclerViewAdapter
 */
abstract class AbstractRecyclerViewAdapter<Data>(context: Context) :
        RecyclerView.Adapter<AbstractRecyclerViewAdapter.ViewHolder>(),
        RecyclerViewAdapterDataManagerInterface<Data>,
        ViewTypeDispatcher {

    companion object {

        /**
         * 要素タイプ：ヘッダ
         */
        val TYPE_HEADER = -1

        /**
         * 要素タイプ：フッタ
         */
        val TYPE_FOOTER = -2

        /**
         * 要素タイプ：EMPTY
         */
        val TYPE_EMPTY = -3

    }

    /**
     * ビューホルダ
     */
    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    /**
     * [LayoutInflater]
     */
    protected val inflater: LayoutInflater = LayoutInflater.from(context)

    /**
     * ヘッダ表示是非
     *
     * @return 表示是非
     */
    protected var displayHeader: Boolean = false

    /**
     * フッタ表示是非
     *
     * @return 表示是非
     */
    protected var displayFooter: Boolean = false

    /**
     * データリスト
     */
    private val mDataList: MutableList<Data> = ArrayList()

    /**
     * データ要素押下リスナ
     *
     * @param <Data> データ要素
     */
    var onItemClick: ((adapter: AbstractRecyclerViewAdapter<Data>, data: Data, clickView: View?) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder? {
        return when (viewType) {
            TYPE_HEADER -> createHeaderViewHolder(inflater, viewGroup)
            TYPE_FOOTER -> createFooterViewHolder(inflater, viewGroup)
            else -> createDataViewHolder(inflater, viewGroup, viewType)
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        var index = position

        val viewType = getItemViewType(position)
        when (viewType) {
            TYPE_HEADER -> bindHeaderViewHolder(viewHolder)
            TYPE_FOOTER -> bindFooterViewHolder(viewHolder)
            else -> {
                if (displayHeader) {
                    // ヘッダが存在する場合はポジションを1減算
                    index--
                }
                bindDataViewHolder(getData(index), viewHolder, viewType)
            }
        }
    }

    override fun getItemCount(): Int {
        var count = mDataList.size
        if (displayHeader) {
            count++
        }
        if (displayFooter) {
            count++
        }
        return count
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionHeader(position)) {
            TYPE_HEADER
        } else if (isPositionFooter(position)) {
            TYPE_FOOTER
        } else if (position >= 0) {
            dispatchDataViewType(position)
        } else {
            TYPE_EMPTY
        }
    }


    // //////////////////////////////////////////////////////////////////////////
    // interface実装メソッド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // その他メソッド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * データ用ViewType振分処理
     *
     * @param position 位置
     * @return データ用ViewType
     */
    @Suppress("MemberVisibilityCanPrivate")
    protected abstract fun dispatchDataViewType(position: Int): Int

    /**
     * ヘッダ用[ViewHolder]生成処理
     *
     * @param inflater [LayoutInflater]
     * @param parent      [ViewGroup]
     * @return [ViewHolder]
     */
    @Suppress("MemberVisibilityCanPrivate")
    protected open fun createHeaderViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder? {
        setHeaderDisplay(true)
        return null
    }

    /**
     * 要素用[ViewHolder]生成処理
     *
     * @param inflater [LayoutInflater]
     * @param parent      [ViewGroup]
     * @param dataViewType データ用ViewType
     * @return [ViewHolder]
     */
    @Suppress("MemberVisibilityCanPrivate")
    protected abstract fun createDataViewHolder(inflater: LayoutInflater, parent: ViewGroup, dataViewType: Int): ViewHolder?

    /**
     * フッタ用[ViewHolder]生成処理
     *
     * @param inflater [LayoutInflater]
     * @param parent      [ViewGroup]
     * @return [ViewHolder]
     */
    @Suppress("MemberVisibilityCanPrivate")
    protected open fun createFooterViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder? {
        setFooterDisplay(true)
        return null
    }

    /**
     * ヘッダ用[ViewHolder]バインド処理
     *
     * @param viewHolder [ViewHolder]
     */
    @Suppress("MemberVisibilityCanPrivate")
    protected open fun bindHeaderViewHolder(viewHolder: ViewHolder) {
        // デフォルト処理なし
    }

    /**
     * 要素用[ViewHolder]バインド処理
     *
     * @param data       [Data]
     * @param viewHolder [ViewHolder]
     * @param dataViewType データ用ViewType
     */
    @Suppress("MemberVisibilityCanPrivate")
    protected abstract fun bindDataViewHolder(data: Data, viewHolder: ViewHolder, dataViewType: Int)

    /**
     * フッタ用[ViewHolder]バインド処理
     *
     * @param viewHolder [ViewHolder]
     */
    @Suppress("MemberVisibilityCanPrivate")
    protected open fun bindFooterViewHolder(viewHolder: ViewHolder) {
        // デフォルト処理なし
    }

    /**
     * ヘッダポジション判定
     *
     *
     * 引き渡されたポジションがヘッダポジションか判定する。
     *
     *
     * @param position 位置
     * @return 判定結果
     */
    @Suppress("MemberVisibilityCanPrivate")
    protected fun isPositionHeader(position: Int): Boolean {
        return displayHeader && position == 0
    }

    /**
     * フッタポジション判定
     *
     *
     * 引き渡されたポジションがフッタポジションか判定する。
     *
     *
     * @param position 位置
     * @return 判定結果
     */
    @Suppress("MemberVisibilityCanPrivate")
    protected fun isPositionFooter(position: Int): Boolean {
        return displayFooter && position == itemCount - 1
    }

    /**
     * ヘッダ表示設定
     *
     * @param display 表示是非
     */
    @Suppress("MemberVisibilityCanPrivate")
    fun setHeaderDisplay(display: Boolean) {
        displayHeader = display
    }

    /**
     * フッタ表示設定
     *
     * @param display 表示是非
     */
    @Suppress("MemberVisibilityCanPrivate")
    fun setFooterDisplay(display: Boolean) {
        displayFooter = display
    }

    override fun getData(position: Int): Data {
        return mDataList[position]
    }

    override fun addData(data: Data) {
        val insertIndex = mDataList.size
        mDataList.add(data)
        notifyItemInserted(insertIndex)
    }

    override fun addDataAll(dataList: Collection<Data>) {
        if (dataList.isEmpty()) return
        val insertIndex = mDataList.size
        mDataList.addAll(dataList)
        notifyItemInserted(insertIndex)
    }

    override fun addDataAll(vararg dataList: Data) {
        if (dataList.isEmpty()) return
        val insertIndex = mDataList.size
        mDataList.addAll(Arrays.asList(*dataList))
        notifyItemInserted(insertIndex)
    }

    override fun clear() {
        mDataList.clear()
        notifyDataSetChanged()
    }

    override val dataCount: Int
        get() = mDataList.size

    override fun getData(itemView: View): Data? {
        val position = getPosition(itemView)
        return if (position != RecyclerView.NO_POSITION
                && !isPositionHeader(position)
                && !isPositionFooter(position)) {
            getData(position - if (displayHeader) 1 else 0)
        } else null
    }

    override fun isFooter(position: Int): Boolean {
        return isPositionFooter(position)
    }

    override fun isHeader(position: Int): Boolean {
        return isPositionHeader(position)
    }

    fun getPosition(itemView: View): Int {
        return (itemView.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
    }

}
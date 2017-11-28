package ns.me.ns.furaffinity.ui.adapter

import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.subjects.PublishSubject
import java.util.*

/**
 * 抽象RecyclerViewAdapter
 */
abstract class AbstractRecyclerViewAdapter<Data>(context: Context) :
        RecyclerView.Adapter<AbstractRecyclerViewAdapter.ViewHolder>(),
        AdapterDataManagerInterface<Data>,
        RecyclerViewPartsInterface {

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

    data class OnClickItem<out Data>(val data: Data, val view: View)

    override var displayHeader: Boolean = false

    override var displayFooter: Boolean = false

    /**
     * ビューホルダ
     */
    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * [LayoutInflater]
     */
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    /**
     * データリスト
     */
    private var items: ObservableList<Data> = ObservableArrayList<Data>()

    /**
     * [WeakReferenceOnListChangedCallback]
     */
    private val onListChangedCallback: WeakReferenceOnListChangedCallback<Data> = WeakReferenceOnListChangedCallback(this)

    /**
     * データ要素押下[PublishSubject]
     */
    val onItemClickPublishSubject: PublishSubject<OnClickItem<Data>>
            = PublishSubject.create()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        items.addOnListChangedCallback(onListChangedCallback)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        items.removeOnListChangedCallback(onListChangedCallback)
        super.onDetachedFromRecyclerView(recyclerView)
    }

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
                getData(index)?.let {
                    bindDataViewHolder(it, viewHolder, viewType)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        var count = items.size
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
        if (display) {
            notifyItemChanged(0)
        } else {
            notifyItemRemoved(0)
        }

    }

    /**
     * フッタ表示設定
     *
     * @param display 表示是非
     */
    @Suppress("MemberVisibilityCanPrivate")
    fun setFooterDisplay(display: Boolean) {
        displayFooter = display
        if (display) {
            notifyItemChanged(itemCount - 1)
        } else {
            notifyItemRemoved(itemCount - 1)
        }

    }

    override fun setData(dataList: Collection<Data>?) {
        if (items == dataList) {
            return
        }
        if (dataList is ObservableList) {
            items = dataList
        } else if (dataList != null) {
            items.clear()
            items.addAll(dataList)
        } else {
            items = ObservableArrayList()
        }

    }

    override fun getData(position: Int): Data? {
        if (position < 0 || position > dataCount - 1) return null
        return items[position]
    }

    override fun addData(data: Data) {
        items.add(data)
    }

    override fun addDataAll(dataList: Collection<Data>) {
        if (dataList.isEmpty()) return
        items.addAll(dataList)
    }

    override fun addDataAll(vararg dataList: Data) {
        if (dataList.isEmpty()) return
        items.addAll(Arrays.asList(*dataList))
    }

    override fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override val dataCount: Int
        get() = items.size

    fun getData(itemView: View): Data? {
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


}
package ns.me.ns.furaffinity.ui.adapter.recycler

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.databinding.ListItemBrowseContentsBinding
import ns.me.ns.furaffinity.databinding.ListItemFooterBinding
import ns.me.ns.furaffinity.repository.model.ViewInterface
import ns.me.ns.furaffinity.repository.model.local.Browse
import ns.me.ns.furaffinity.repository.model.remote.entity.ImageElement
import ns.me.ns.furaffinity.repository.model.remote.entity.UserElement
import ns.me.ns.furaffinity.ui.ObservableDrawableTarget
import ns.me.ns.furaffinity.ui.adapter.AbstractRecyclerViewAdapter

/**
 * Browse Adapter
 */
class BrowseAdapter(context: Context) : AbstractRecyclerViewAdapter<BrowseAdapter.ContentsViewModel>(context) {

    companion object {

        /**
         * 要素タイプ：データ(コンテンツ)
         */
        val TYPE_DATA_CONTENTS = 0

    }

    /**
     * セル高さ
     */
    private var cellHeight: Int? = null

    /**
     * リロード要求
     */
    var onRequestReload: (() -> Unit)? = null

    /**
     * エラー是非
     */
    private var error: Boolean = false

    /**
     * データコンテンツViewModel
     */
    class ContentsViewModel(value: Browse) : ViewInterface {

        override var viewId: Int = value.viewId

        override var name: String? = value.name

        override val image: ObservableDrawableTarget = value.image

        override val imageElement: ObservableField<ImageElement> = value.imageElement

        override val userElement: ObservableField<UserElement> = ObservableField<UserElement>()

        var onItemSelected: View.OnClickListener? = null
    }

    /**
     * データコンテンツ[AbstractRecyclerViewAdapter.ViewHolder]
     */
    class ContentsViewHolder(itemView: View, cellHeight: Int?) : AbstractRecyclerViewAdapter.ViewHolder(itemView) {
        val binding: ListItemBrowseContentsBinding = DataBindingUtil.bind(itemView)

        init {
            val lp = binding.imageView.layoutParams
            lp.height = cellHeight ?: lp.height
            binding.imageView.layoutParams = lp
        }
    }

    /**
     * フッター[AbstractRecyclerViewAdapter.ViewHolder]
     */
    class FooterHolder(itemView: View) : AbstractRecyclerViewAdapter.ViewHolder(itemView) {
        val binding: ListItemFooterBinding = DataBindingUtil.bind(itemView)
    }

    override fun dispatchDataViewType(position: Int): Int = TYPE_DATA_CONTENTS

    override fun createDataViewHolder(inflater: LayoutInflater, parent: ViewGroup, dataViewType: Int): AbstractRecyclerViewAdapter.ViewHolder? =
            when (dataViewType) {
                TYPE_DATA_CONTENTS -> ContentsViewHolder(inflater.inflate(R.layout.list_item_browse_contents, parent, false), cellHeight)
                else -> null
            }

    override fun bindDataViewHolder(data: ContentsViewModel, viewHolder: AbstractRecyclerViewAdapter.ViewHolder, dataViewType: Int) {
        when (dataViewType) {
            TYPE_DATA_CONTENTS -> {
                (viewHolder as? ContentsViewHolder)?.let {
                    val view = it.binding.imageView
                    data.onItemSelected = View.OnClickListener { onItemClickPublishSubject.onNext(OnClickItem(data, view)) }
                    it.binding.viewModel = data
                }
            }
        }
    }

    override fun bindFooterViewHolder(viewHolder: ViewHolder) {
        (viewHolder as? FooterHolder)?.let {
            it.binding.isError = error
            it.binding.onRetryClickListener = View.OnClickListener { _ ->
                error = false
                it.binding.isError = error
                onRequestReload?.invoke()
            }
        }
    }

    override fun createFooterViewHolder(inflater: LayoutInflater, parent: ViewGroup): AbstractRecyclerViewAdapter.ViewHolder? {
        return FooterHolder(inflater.inflate(R.layout.list_item_footer, parent, false))
    }

    /**
     * エラー設定処理
     */
    fun setLoadingError(isError: Boolean) {
        error = isError
    }

    /**
     * 高さの決定
     */
    fun determinationCellHeight(context: Context, count: Int) {
        cellHeight = context.resources.displayMetrics.widthPixels / count
    }

}
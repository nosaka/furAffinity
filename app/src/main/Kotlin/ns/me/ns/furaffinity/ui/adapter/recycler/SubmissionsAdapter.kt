package ns.me.ns.furaffinity.ui.adapter.recycler

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.databinding.ListItemFooterBinding
import ns.me.ns.furaffinity.databinding.ListItemSubmissionsContentsBinding
import ns.me.ns.furaffinity.ds.local.model.Submission
import ns.me.ns.furaffinity.ds.remote.model.impl.entity.ViewElement
import ns.me.ns.furaffinity.ui.adapter.AbstractRecyclerViewAdapter

/**
 * Submissions Adapter
 */
class SubmissionsAdapter(context: Context) : AbstractRecyclerViewAdapter<SubmissionsAdapter.ContentsViewModel>(context) {

    companion object {

        /**
         * 要素タイプ：データ(コンテンツ)
         */
        val TYPE_DATA_CONTENTS = 0

    }

    /**
     * ローディングエラー是非
     */
    var loadingError: Boolean = false
        set(value) {
            field = value
            setFooterDisplay(true)
        }

    /**
     * リロード要求
     */
    var onRequestReload: (() -> Unit)? = null

    /**
     * データコンテンツViewModel
     */
    class ContentsViewModel(value: Submission) : ViewElement() {
        init {
            viewId = value.viewId
            name = value.name

            imageElement.src = value.src
            imageElement.alt = value.alt

        }

        var onItemSelected: View.OnClickListener? = null
    }

    /**
     * データコンテンツ[AbstractRecyclerViewAdapter.ViewHolder]
     */
    class ContentsViewHolder(itemView: View) : AbstractRecyclerViewAdapter.ViewHolder(itemView) {
        val binding: ListItemSubmissionsContentsBinding = DataBindingUtil.bind(itemView)

        init {
            val lp = binding.imageView.layoutParams
            lp.height = itemView.context.resources.displayMetrics.widthPixels / 3
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

    override fun createDataViewHolder(inflater: LayoutInflater, parent: ViewGroup, dataViewType: Int): AbstractRecyclerViewAdapter.ViewHolder? {
        return when (dataViewType) {
            TYPE_DATA_CONTENTS -> ContentsViewHolder(inflater.inflate(R.layout.list_item_submissions_contents, parent, false))
            else -> null
        }
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
            it.binding.loadingError = loadingError
            it.binding.onRetryClickListener = View.OnClickListener { _ ->
                loadingError = false
                it.binding.loadingError = loadingError
                onRequestReload?.invoke()
            }
        }
    }

    override fun createFooterViewHolder(inflater: LayoutInflater, parent: ViewGroup): AbstractRecyclerViewAdapter.ViewHolder? {
        return FooterHolder(inflater.inflate(R.layout.list_item_footer, parent, false))
    }

}
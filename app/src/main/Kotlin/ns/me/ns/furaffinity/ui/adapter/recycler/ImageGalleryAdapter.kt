package ns.me.ns.furaffinity.ui.adapter.recycler

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.databinding.ListItemImageGalleryContentsBinding
import ns.me.ns.furaffinity.databinding.ListItemImageGalleryPageBinding
import ns.me.ns.furaffinity.ui.adapter.AbstractRecyclerViewAdapter

/**
 * 画像ギャラリAdapter
 */
class ImageGalleryAdapter(context: Context) : AbstractRecyclerViewAdapter<ImageGalleryAdapter.ViewModel>(context) {

    init {
        setFooterDisplay(true)
    }

    companion object {

        /**
         * 要素タイプ：データ(コンテンツ)
         */
        val TYPE_DATA_CONTENTS = 0

        /**
         * 要素タイプ：データ(ページ)
         */
        val TYPE_DATA_PAGE = 1

        /**
         * 要素タイプ：データ(コンテンツ)
         */
        val TYPE_DATA_UNKNOWN = 2

    }

    class ViewModel {
        var dataViewType: Int = TYPE_DATA_UNKNOWN
        var description: String? = null
        var imageSrc: String? = null
    }

    class ContentsViewHolder(itemView: View) : AbstractRecyclerViewAdapter.ViewHolder(itemView) {
        val binding: ListItemImageGalleryContentsBinding = DataBindingUtil.bind(itemView)

        init {
            val lp = binding.root.layoutParams
            lp.height = itemView.context.resources.displayMetrics.widthPixels / 3
            binding.root.layoutParams = lp
        }
    }

    class PageViewHolder(itemView: View) : AbstractRecyclerViewAdapter.ViewHolder(itemView) {
        val binding: ListItemImageGalleryPageBinding = DataBindingUtil.bind(itemView)

        init {
            val lp = binding.root.layoutParams
            lp.height = itemView.context.resources.displayMetrics.widthPixels / 3
            binding.root.layoutParams = lp
        }
    }

    override fun dispatchDataViewType(position: Int): Int = getData(position).dataViewType

    override fun createDataViewHolder(inflater: LayoutInflater, parent: ViewGroup, dataViewType: Int): AbstractRecyclerViewAdapter.ViewHolder? {
        return when (dataViewType) {
            TYPE_DATA_CONTENTS -> ContentsViewHolder(inflater.inflate(R.layout.list_item_image_gallery_contents, parent, false))
            TYPE_DATA_PAGE -> PageViewHolder(inflater.inflate(R.layout.list_item_image_gallery_page, parent, false))
            else -> null
        }
    }

    override fun bindDataViewHolder(data: ViewModel, viewHolder: AbstractRecyclerViewAdapter.ViewHolder, dataViewType: Int) {
        when (dataViewType) {
            TYPE_DATA_CONTENTS -> {
                (viewHolder as? ContentsViewHolder)?.let {
                    it.binding.viewModel = data
                }
            }
            TYPE_DATA_PAGE -> {
                (viewHolder as? PageViewHolder)?.let {
                    it.binding.viewModel = data
                }
            }
        }
    }

    override fun createFooterViewHolder(inflater: LayoutInflater, parent: ViewGroup): AbstractRecyclerViewAdapter.ViewHolder? {
        super.createFooterViewHolder(inflater, parent)
        val progressBar = ProgressBar(parent.context)
        return object : AbstractRecyclerViewAdapter.ViewHolder(progressBar) {}
    }

}
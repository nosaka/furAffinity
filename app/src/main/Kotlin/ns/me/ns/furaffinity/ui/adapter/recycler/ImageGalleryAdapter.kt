package ns.me.ns.furaffinity.ui.adapter.recycler

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.subjects.PublishSubject
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.databinding.ListItemImageGalleryContentsBinding
import ns.me.ns.furaffinity.datasouce.web.model.impl.entity.ViewElement
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

    }

    class ViewModel(viewElement: ViewElement) : ViewElement() {
        init {
            viewId = viewElement.viewId
            name = viewElement.name
            href = viewElement.href
            imageElement = viewElement.imageElement
            userElement = viewElement.userElement
        }

        val onItemClickPublishSubject: PublishSubject<ImageGalleryAdapter.ViewModel>
                = PublishSubject.create<ImageGalleryAdapter.ViewModel>()

        val getOnItemSelected = View.OnClickListener { _ ->
            onItemClickPublishSubject.onNext(this)
        }
    }

    class ContentsViewHolder(itemView: View) : AbstractRecyclerViewAdapter.ViewHolder(itemView) {
        val binding: ListItemImageGalleryContentsBinding = DataBindingUtil.bind(itemView)

        init {
            val lp = binding.imageView.layoutParams
            lp.height = itemView.context.resources.displayMetrics.widthPixels / 3
            binding.imageView.layoutParams = lp
        }
    }

    class FooterHolder(itemView: View) : AbstractRecyclerViewAdapter.ViewHolder(itemView)

    override fun dispatchDataViewType(position: Int): Int = TYPE_DATA_CONTENTS

    override fun createDataViewHolder(inflater: LayoutInflater, parent: ViewGroup, dataViewType: Int): AbstractRecyclerViewAdapter.ViewHolder? {
        return when (dataViewType) {
            TYPE_DATA_CONTENTS -> ContentsViewHolder(inflater.inflate(R.layout.list_item_image_gallery_contents, parent, false))
            else -> null
        }
    }

    override fun bindDataViewHolder(data: ViewModel, viewHolder: AbstractRecyclerViewAdapter.ViewHolder, dataViewType: Int) {
        when (dataViewType) {
            TYPE_DATA_CONTENTS -> {
                (viewHolder as? ContentsViewHolder)?.let {
                    val view = it.binding.imageView
                    it.binding.viewModel = data
                    it.binding.viewModel.onItemClickPublishSubject.subscribe {
                        onItemClick?.invoke(this@ImageGalleryAdapter, it, view)
                    }
                }
            }
        }
    }

    override fun createFooterViewHolder(inflater: LayoutInflater, parent: ViewGroup): AbstractRecyclerViewAdapter.ViewHolder? {
        return FooterHolder(inflater.inflate(R.layout.list_item_footer, parent, false))
    }

}
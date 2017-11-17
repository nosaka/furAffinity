package ns.me.ns.furaffinity.ui.adapter.recycler

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.subjects.PublishSubject
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.databinding.ListItemFavoriteContentsBinding
import ns.me.ns.furaffinity.ds.local.model.Favorite
import ns.me.ns.furaffinity.ui.adapter.AbstractRecyclerViewAdapter

/**
 * Submissions Adapter
 */
class FavoriteAdapter(context: Context) : AbstractRecyclerViewAdapter<FavoriteAdapter.ContentsViewModel>(context) {

    init {
        setFooterDisplay(true)
    }

    companion object {

        /**
         * 要素タイプ：データ(コンテンツ)
         */
        val TYPE_DATA_CONTENTS = 0

    }

    /**
     * データコンテンツViewModel
     */
    class ContentsViewModel(value: Favorite) : Favorite() {
        init {
            viewId = value.viewId
            src = value.src
            imageData = value.imageData
            alt = value.alt
        }

        val onItemClickPublishSubject: PublishSubject<ContentsViewModel>
                = PublishSubject.create<ContentsViewModel>()

        val onItemSelected: View.OnClickListener = View.OnClickListener { onItemClickPublishSubject.onNext(this@ContentsViewModel) }
    }

    /**
     * データコンテンツ[AbstractRecyclerViewAdapter.ViewHolder]
     */
    class ContentsViewHolder(itemView: View) : ViewHolder(itemView) {
        val binding: ListItemFavoriteContentsBinding = DataBindingUtil.bind(itemView)
    }

    override fun dispatchDataViewType(position: Int): Int = TYPE_DATA_CONTENTS

    override fun createDataViewHolder(inflater: LayoutInflater, parent: ViewGroup, dataViewType: Int): ViewHolder? {
        return when (dataViewType) {
            TYPE_DATA_CONTENTS -> ContentsViewHolder(inflater.inflate(R.layout.list_item_favorite_contents, parent, false))
            else -> null
        }
    }

    override fun bindDataViewHolder(data: ContentsViewModel, viewHolder: ViewHolder, dataViewType: Int) {
        when (dataViewType) {
            TYPE_DATA_CONTENTS -> {
                (viewHolder as? ContentsViewHolder)?.let {
                    val view = it.binding.imageView
                    it.binding.viewModel = data
                    it.binding.viewModel.onItemClickPublishSubject.subscribe {
                        onItemClick?.invoke(this@FavoriteAdapter, it, view)
                    }
                }
            }
        }
    }

}
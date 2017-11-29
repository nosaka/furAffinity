package ns.me.ns.furaffinity.ui.adapter.recycler

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.databinding.ListItemFavoriteContentsBinding
import ns.me.ns.furaffinity.di.compornent.DaggerModuleComponent
import ns.me.ns.furaffinity.di.modules.ContextModule
import ns.me.ns.furaffinity.repository.FavoriteRepository
import ns.me.ns.furaffinity.repository.model.local.Favorite
import ns.me.ns.furaffinity.ui.adapter.AbstractRecyclerViewAdapter
import ns.me.ns.furaffinity.util.LogUtil
import javax.inject.Inject

/**
 * Submissions Adapter
 */
class FavoriteAdapter(context: Context) : AbstractRecyclerViewAdapter<FavoriteAdapter.ContentsViewModel>(context) {

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

        var onItemSelected: View.OnClickListener? = null

        var onCloseClick: View.OnClickListener? = null
    }

    /**
     * データコンテンツ[AbstractRecyclerViewAdapter.ViewHolder]
     */
    class ContentsViewHolder(itemView: View) : ViewHolder(itemView) {
        val binding: ListItemFavoriteContentsBinding = DataBindingUtil.bind(itemView)
    }

    init {
        DaggerModuleComponent.builder()
                .contextModule(ContextModule(context))
                .build()
                .inject(this)
    }

    @Inject
    lateinit var favoriteRepository: FavoriteRepository

    val onEmptyItemPublishSubject: PublishSubject<Unit>
            = PublishSubject.create()

    override fun dispatchDataViewType(position: Int): Int = TYPE_DATA_CONTENTS

    override fun createDataViewHolder(inflater: LayoutInflater, parent: ViewGroup, dataViewType: Int): ViewHolder? =
            when (dataViewType) {
                TYPE_DATA_CONTENTS -> ContentsViewHolder(inflater.inflate(R.layout.list_item_favorite_contents, parent, false))
                else -> null
            }

    override fun bindDataViewHolder(data: ContentsViewModel, viewHolder: ViewHolder, dataViewType: Int) {
        when (dataViewType) {
            TYPE_DATA_CONTENTS -> {
                (viewHolder as? ContentsViewHolder)?.let {
                    val view = it.binding.imageView
                    data.onItemSelected = View.OnClickListener { onItemClickPublishSubject.onNext(OnClickItem(data, view)) }
                    data.onCloseClick = View.OnClickListener {
                        removeData(data)
                    }
                    it.binding.viewModel = data
                }
            }
        }
    }

    override fun removeData(data: ContentsViewModel) {
        favoriteRepository.remove(data.viewId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    super.removeData(data)
                    if (dataCount <= 0) onEmptyItemPublishSubject.onNext(Unit)
                }, {
                    LogUtil.e(it)
                })
    }

}
package ns.me.ns.furaffinity.ui.adapter.pager

import android.content.Context
import android.databinding.*
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.databinding.ListItemFullViewBinding
import ns.me.ns.furaffinity.di.compornent.DaggerModuleComponent
import ns.me.ns.furaffinity.di.modules.ContextModule
import ns.me.ns.furaffinity.ds.webapi.AppWebApiService
import ns.me.ns.furaffinity.repository.model.ViewInterface
import ns.me.ns.furaffinity.repository.model.remote.Full
import ns.me.ns.furaffinity.repository.model.remote.entity.ImageElement
import ns.me.ns.furaffinity.repository.model.remote.entity.UserElement
import ns.me.ns.furaffinity.ui.ObservableDrawableTarget
import ns.me.ns.furaffinity.ui.adapter.AdapterDataManagerInterface
import ns.me.ns.furaffinity.util.LogUtil
import javax.inject.Inject

/**
 * 拡大画面画像PagerAdapter
 */
class FullViewPagerAdapter(val context: Context) : PagerAdapter(), AdapterDataManagerInterface<ViewInterface> {

    init {
        DaggerModuleComponent.builder()
                .contextModule(ContextModule(context))
                .build()
                .inject(this)
    }

    @Inject
    lateinit var service: AppWebApiService

    /**
     * データコンテンツViewModel
     */
    inner class ViewModel(value: ViewInterface) : ViewInterface {

        override var viewId: Int = value.viewId

        override var name: String? = value.name

        override val image: ObservableDrawableTarget = value.image

        override val imageElement: ObservableField<ImageElement> = value.imageElement

        override val userElement: ObservableField<UserElement> = value.userElement

        private val onPropertyChangedCallback: Observable.OnPropertyChangedCallback by lazy {
            object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(observable: Observable?, id: Int) {
                    when (observable) {
                        full -> {
                            loadImage()
                            full.get()?.userElement?.let {
                                userElement.set(UserElement(it.account))
                            }
                        }
                    }
                }
            }
        }

        private val full: ObservableField<Full> = ObservableField<Full>().apply {
            addOnPropertyChangedCallback(onPropertyChangedCallback)
        }

        val onPhotoTapListener = OnPhotoTapListener { view, x, y ->
            onPhotoTap?.invoke(viewId)
        }

        fun getFullIfNecessary() {
            if (full.get() != null) {
                return
            }
            loadImage()
            service.getFull(viewId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        full.set(it)
                    }, {
                        LogUtil.e(it)
                    })
        }

        private fun loadImage() {
            val url = full.get()?.imageElement?.src ?: imageElement.get().src
            Picasso.with(context).load(url).placeholder(image.get()).into(image)
        }

    }

    /**
     * [LayoutInflater]
     */
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    /**
     * データリスト
     */
    private var items: ObservableList<ViewModel> = ObservableArrayList<ViewModel>()

    /**
     * 写真押下リスナ
     */
    var onPhotoTap: ((viewId: Int) -> Unit)? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        // リストから取得
        val viewModel = items[position]
        val binding: ListItemFullViewBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_full_view, container, false)
        binding.photoView.scale = 1f
        binding.viewModel = viewModel

        viewModel.getFullIfNecessary()



        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // コンテナから View を削除
        container.removeView(`object` as View)
    }

    override fun getCount(): Int = items.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun setData(dataList: Collection<ViewInterface>?) {
        if (items == dataList) {
            return
        }
        if (dataList != null) {
            items.clear()
            items.addAll(dataList.map { ViewModel(it) })
        } else {
            items = ObservableArrayList()
        }
    }

    override fun getData(position: Int): ViewInterface? {
        if (position < 0 || position > dataCount - 1) return null
        return items[position]
    }

    override fun addData(data: ViewInterface) {
        items.add(ViewModel(data))
    }

    override fun addDataAll(dataList: Collection<ViewInterface>) {
        if (dataList.isEmpty()) return
        items.addAll(dataList.map { ViewModel(it) })
    }

    override fun addDataAll(vararg dataList: ViewInterface) {
        if (dataList.isEmpty()) return
        items.addAll(dataList.map { ViewModel(it) })
    }

    override fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override val dataCount
        get() = count

    fun findPosition(viewId: Int): Int = items.indexOfFirst { it.viewId == viewId }

}

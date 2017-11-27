package ns.me.ns.furaffinity.ui.adapter.pager

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.databinding.ListItemFullViewBinding
import ns.me.ns.furaffinity.repository.model.ViewImageInterface

/**
 * 拡大画面画像PagerAdapter
 */
class FullViewPagerAdapter(context: Context) : PagerAdapter() {

    /**
     * データコンテンツViewModel
     */
    class ViewModel(value: ViewImageInterface) : ViewImageInterface {

        override var viewId: Int = value.viewId
        override var src: String? = value.src
        override var alt: String? = value.alt

    }

    /**
     * [LayoutInflater]
     */
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    /**
     * 要素
     */
    val items: ArrayList<ViewModel> = ArrayList()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        // リストから取得
        val item = items[position]
        val binding: ListItemFullViewBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_full_view, container, false)
        binding.photoView.scale = 1f
        binding.viewModel = item
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // コンテナから View を削除
        container.removeView(`object` as View)
    }

    override fun getCount(): Int = items.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

}

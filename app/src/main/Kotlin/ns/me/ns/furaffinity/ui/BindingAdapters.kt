package ns.me.ns.furaffinity.ui

import android.databinding.BindingAdapter
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.squareup.picasso.Picasso
import ns.me.ns.furaffinity.ui.adapter.OnEndScrollListener
import ns.me.ns.furaffinity.ui.adapter.recycler.decoration.ItemDecorationGrid


object BindingAdapters {

    /**
     * 画像ロード処理
     */
    @BindingAdapter("loadImg")
    @JvmStatic
    fun loadImg(view: ImageView, oldUrl: String?, newUrl: String?) {
        if (!newUrl.equals(oldUrl)) {
            Picasso.with(view.context).load(newUrl).into(view)
        }
    }

    /**
     * 画像ロード処理
     */
    @BindingAdapter("loadPlaceHolderImage")
    @JvmStatic
    fun loadPlaceHolderImage(view: ImageView, oldUrl: String?, newUrl: String?) {
        if (!newUrl.equals(oldUrl)) {
            Picasso.with(view.context).load(newUrl).placeholder(view.drawable).into(view)
        }
    }

    @BindingAdapter("onEndScrollListener")
    @JvmStatic
    fun setOnScrollListener(view: RecyclerView, listener: OnEndScrollListener) {
        view.clearOnScrollListeners()
        view.addOnScrollListener(listener)
    }

    @BindingAdapter("gridDivider")
    @JvmStatic
    fun gridDivider(recyclerView: RecyclerView, dividerSize: Float) {
        (recyclerView.layoutManager as? GridLayoutManager)?.let {
            recyclerView.addItemDecoration(ItemDecorationGrid(it.spanCount, dividerSize))
        }

    }
}

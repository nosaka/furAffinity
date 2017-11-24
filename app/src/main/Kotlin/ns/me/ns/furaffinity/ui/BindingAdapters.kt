package ns.me.ns.furaffinity.ui

import android.databinding.BindingAdapter
import android.databinding.BindingMethod
import android.databinding.BindingMethods
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.github.chrisbanes.photoview.PhotoView
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ns.me.ns.furaffinity.ui.adapter.OnEndScrollListener
import ns.me.ns.furaffinity.ui.adapter.recycler.decoration.ItemDecorationGrid
import ns.me.ns.furaffinity.ui.adapter.recycler.decoration.ItemDecorationLinear
import ns.me.ns.furaffinity.util.BitmapUtil
import ns.me.ns.furaffinity.util.LogUtil


@BindingMethods(
        BindingMethod(type = PhotoView::class, attribute = "onPhotoTapListener", method = "setOnPhotoTapListener"),
        BindingMethod(type = PhotoView::class, attribute = "onSingleFlingListener", method = "setOnSingleFlingListener"),
        BindingMethod(type = BottomNavigationView::class, attribute = "onNavigationItemSelectedListener", method = "setOnNavigationItemSelectedListener"),
        BindingMethod(type = FloatingActionButton::class, attribute = "icon", method = "setImageResource"),
        BindingMethod(type = SwipeRefreshLayout::class, attribute = "onRefreshListener", method = "setOnRefreshListener"),
        BindingMethod(type = SwipeRefreshLayout::class, attribute = "refreshing", method = "setRefreshing")
)
object BindingAdapters {

    /**
     * 画像ロード処理
     */
    @BindingAdapter("loadImg")
    @JvmStatic
    fun loadImg(view: ImageView, data: ByteArray?) {
        data?.let {
            BitmapUtil.getBitmap(it)
                    .observeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        view.setImageBitmap(it)
                    }, {
                        LogUtil.e(it)
                    })
        }
    }

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

    @BindingAdapter("linearDivider")
    @JvmStatic
    fun linearDivider(recyclerView: RecyclerView, dividerSize: Float) {
        (recyclerView.layoutManager as? LinearLayoutManager)?.let {
            recyclerView.addItemDecoration(ItemDecorationLinear(it.orientation, dividerSize))
        }
    }
}

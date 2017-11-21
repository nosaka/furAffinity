package ns.me.ns.furaffinity.ui.adapter

import android.databinding.ObservableList
import java.lang.ref.WeakReference


/**
 * アダプタコールバック&データセット変更クラス
 */
class WeakReferenceOnListChangedCallback<T>(adapter: AbstractRecyclerViewAdapter<*>) : ObservableList.OnListChangedCallback<ObservableList<T>>() {

    private var adapterReference = WeakReference(adapter)

    override fun onItemRangeMoved(sender: ObservableList<T>, fromPosition: Int, toPosition: Int, itemCount: Int) {
        adapterReference.get()?.notifyItemMoved(fromPosition, toPosition)
    }

    override fun onChanged(sender: ObservableList<T>) {
        adapterReference.get()?.notifyDataSetChanged()
    }

    override fun onItemRangeChanged(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
        adapterReference.get()?.notifyItemRangeChanged(positionStart, itemCount)
    }

    override fun onItemRangeInserted(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
        // TODO : adapterReference.get()?.notifyItemRangeInserted(positionStart, itemCount) だと勝手にスクロールする。要調査
        adapterReference.get()?.notifyItemInserted(positionStart)
    }

    override fun onItemRangeRemoved(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
        adapterReference.get()?.notifyItemRangeRemoved(positionStart, itemCount)
    }

}
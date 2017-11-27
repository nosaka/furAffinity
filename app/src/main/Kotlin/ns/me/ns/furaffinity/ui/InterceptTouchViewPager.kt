package ns.me.ns.furaffinity.ui

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * InterceptTouchViewPager
 */
class InterceptTouchViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    interface Listener {
        fun onStartDrag() {}
        fun onDragging(dx: Float, dy: Float) {}
        fun onFinishDrag(dx: Float, dy: Float) {}
    }

    private var beginX: Float? = null

    private var beginY: Float? = null

    private var prevDragX: Float? = null

    private var prevDragY: Float? = null

    var listener: Listener? = null

    var scrollState: Int = SCROLL_STATE_IDLE

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            scrollState = state
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            // 処理なし
        }

        override fun onPageSelected(position: Int) {
            // 処理なし
        }

    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        synchronized(e) {
            when (e.action) {
                MotionEvent.ACTION_DOWN -> {
                    listener?.onStartDrag()
                    saveStartDrag(e)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    val beginX = beginX ?: return false
                    val beginY = beginY ?: return false
                    val dx = e.rawX - beginX
                    val dy = e.rawY - beginY
                    listener?.onFinishDrag(dx, dy)
                    // ドラッグ座標のクリア
                    clearDrag()
                }
                MotionEvent.ACTION_MOVE -> {
                    if (scrollState != SCROLL_STATE_IDLE) return false
                    val prevDragX = prevDragX ?: return false
                    val prevDragY = prevDragY ?: return false
                    val dx = e.rawX - prevDragX
                    val dy = e.rawY - prevDragY
                    listener?.onDragging(dx, dy)
                    // 前回のイベントの座標を記録
                    saveDragging(e)
                }
            }

            return super.onInterceptTouchEvent(e)
        }
    }

    override fun onAttachedToWindow() {
        addOnPageChangeListener(onPageChangeListener)
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeOnPageChangeListener(onPageChangeListener)
    }

    private fun saveStartDrag(e: MotionEvent) {
        beginX = e.rawX
        beginY = e.rawY
        prevDragX = e.rawX
        prevDragY = e.rawY

    }

    private fun saveDragging(e: MotionEvent) {
        prevDragX = e.rawX
        prevDragY = e.rawY
    }

    private fun clearDrag() {
        beginX = null
        beginY = null
        prevDragX = null
        prevDragY = null
    }


}
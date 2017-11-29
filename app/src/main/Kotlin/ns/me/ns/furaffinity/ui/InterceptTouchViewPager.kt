package ns.me.ns.furaffinity.ui

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * InterceptTouchViewPager
 */
class InterceptTouchViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    interface OnInterceptDragListener {
        fun onStartDrag() {}
        fun onDragging(dx: Float, dy: Float) {}
        fun onFinishDrag(dx: Float, dy: Float) {}
    }

    private var distanceX: Float = 0f

    private var distanceY: Float = 0f

    private var prevDragX: Float? = null

    private var prevDragY: Float? = null

    private var onInterceptDragListener: OnInterceptDragListener? = null

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        val result = super.onInterceptTouchEvent(e)

        synchronized(e) {

            if (e.pointerCount != 1) {
                onInterceptDragListener?.onFinishDrag(0f, 0f)
                // ドラッグ座標のクリア
                distanceX = 0f
                distanceY = 0f
                prevDragX = null
                prevDragY = null
                return result
            }

            when (e.action) {
                MotionEvent.ACTION_DOWN -> {
                    distanceX = 0f
                    distanceY = 0f
                    prevDragX = e.rawX
                    prevDragY = e.rawY
                    onInterceptDragListener?.onStartDrag()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    onInterceptDragListener?.onFinishDrag(distanceX, distanceY)
                    // ドラッグ座標のクリア
                    distanceX = 0f
                    distanceY = 0f
                    prevDragX = null
                    prevDragY = null
                }
                MotionEvent.ACTION_MOVE -> {
                    val prevRawX = prevDragX ?: return false
                    val prevRawY = prevDragY ?: return false

                    val dx = e.rawX - prevRawX
                    val dy = e.rawY - prevRawY
                    prevDragX = e.rawX
                    prevDragY = e.rawY
                    distanceX += dx
                    distanceY += dy
                    onInterceptDragListener?.onDragging(dx, dy)
                }
            }

            return result
        }
    }


    fun setOnInterceptDragListener(listenerIntercept: OnInterceptDragListener) {
        onInterceptDragListener = listenerIntercept
    }


}
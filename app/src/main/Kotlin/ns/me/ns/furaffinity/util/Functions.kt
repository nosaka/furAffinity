package ns.me.ns.furaffinity.util

import android.content.Context


fun Context.dp2Px(dp: Float): Float {
    val metrics = this.resources.displayMetrics
    return dp * metrics.density
}

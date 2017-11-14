package ns.me.ns.furaffinity

import android.content.ContextWrapper
import android.content.pm.PackageManager

/**
 * [ContextWrapper.checkSelfPermission]拡張
 * @param permissions パーミッションリスト
 * @return 非許可パーミッションリスト
 */
fun ContextWrapper.checkSelfPermission(vararg permissions: String): ArrayList<String> {
    val denied = ArrayList<String>()
    permissions.forEach {
        if (this.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
            denied.add(it)
        }
    }
    return denied
}
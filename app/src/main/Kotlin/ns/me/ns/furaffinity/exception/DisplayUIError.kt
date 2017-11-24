package ns.me.ns.furaffinity.exception

import android.support.annotation.StringRes

/**
 * UI通知用エラー
 */
class DisplayUIError(@StringRes val messageRes: Int) : Error() {


}
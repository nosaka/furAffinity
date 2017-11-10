package ns.me.ns.furaffinity.util

import android.content.Context
import me.qrio.smartlock2.sdk.util.PreferencesHelper

/**
 * 汎用Preferenceヘルパー
 * このクラスのメソッドはロジックから直接呼ばないで下さい。
 */
class LoginManager private constructor() : PreferencesHelper {

    companion object {
        const val KEY_COOKIE = "cookie"

        val instance: LoginManager by lazy { LoginManager() }
    }

    override val preferencesName: String = javaClass.name

    fun getCookie(context: Context) = getString(context, KEY_COOKIE, null)

    fun setCookie(context: Context, value: String?) = putString(context, KEY_COOKIE, value)

}
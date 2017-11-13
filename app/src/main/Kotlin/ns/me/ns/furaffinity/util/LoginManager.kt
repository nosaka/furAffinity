package ns.me.ns.furaffinity.util

import android.content.Context
import android.webkit.CookieManager
import me.qrio.smartlock2.sdk.util.PreferencesHelper
import ns.me.ns.furaffinity.Constants
import okhttp3.Cookie

/**
 * LoginManager
 */
class LoginManager private constructor() : PreferencesHelper {

    companion object {
        const val KEY_COOKIE = "cookie"

        val instance: LoginManager by lazy { LoginManager() }
    }

    override val preferencesName: String = javaClass.name

    fun getCookie(context: Context): List<Cookie>? {
        val value = getString(context, KEY_COOKIE, null) ?: return emptyList()
        return value.split(";").map { it.trim().split("=") }.filter { it.count() == 2 }.map {
            return@map Cookie.Builder()
                    .domain(Constants.DOMAIN)
                    .name(it[0])
                    .value(it[1])
                    .build()
        }
    }

    fun setCookie(context: Context, url: String?) {
        val cookie = CookieManager.getInstance().getCookie(url)
        putString(context, KEY_COOKIE, cookie)
    }

}
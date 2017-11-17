package ns.me.ns.furaffinity.exception

import ns.me.ns.furaffinity.ds.remote.parser.JsoupParser
import javax.security.auth.login.LoginException
import kotlin.reflect.KClass

/**
 * ログイン必須例外
 */
class LoginRequiredException(clazz:KClass<out JsoupParser<*>>) : LoginException("$clazz is login required."){
}
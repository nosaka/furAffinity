package ns.me.ns.furaffinity.datasouce.web.model

import org.apache.commons.lang3.builder.ToStringBuilder

/**
 *
 */
abstract class JsoupPOJO {

    var isLogin:Boolean = false

    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this)
    }
}
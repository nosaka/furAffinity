package ns.me.ns.furaffinity.repository.model

import org.apache.commons.lang3.builder.ToStringBuilder

/**
 * 基底Jsoup Response
 */
abstract class AbstractBaseJsoupResponse {

    var isLogin: Boolean = false

    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this)
    }
}
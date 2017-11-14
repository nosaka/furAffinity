package ns.me.ns.furaffinity.datasouce.web.parser

import ns.me.ns.furaffinity.datasouce.web.model.AbstractBaseJsoupResponse
import ns.me.ns.furaffinity.exception.LoginRequiredException
import org.jsoup.nodes.Document

/**
 * JsoupParser
 */
interface JsoupParser<T> {

    val requiredLogin: Boolean

    fun isLogin(document: Document): Boolean {
        if (document.getElementById("logout-href") != null || document.getElementById("logout-link") != null) {
            return true
        }

        return false
    }

    fun parseDocument(document: Document, data: T?): T? {
        val result = data as? AbstractBaseJsoupResponse
        result?.isLogin = isLogin(document)

        if (result?.isLogin != true && requiredLogin) {
            // 必須ログインにも関わらずログインしていない場合は例外処理
            throw LoginRequiredException(this::class)

        }

        return parse(document, data)
    }

    fun parse(document: Document, data: T?): T?

}
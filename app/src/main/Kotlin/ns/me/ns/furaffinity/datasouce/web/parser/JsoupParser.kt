package ns.me.ns.furaffinity.datasouce.web.parser

import ns.me.ns.furaffinity.datasouce.web.model.JsoupPOJO
import org.jsoup.nodes.Document

/**
 * JsoupParser
 */
interface JsoupParser {

    fun isLogin(document: Document): Boolean {
        val noBlock = document.select(".header_bkg .noblock")?.first()
        if (noBlock?.getElementsByAttributeValueMatching("href", "/logout/$") != null) {
            return true
        }

        return false
    }

    fun <T> parseDocument(document: Document, data: T?): T? {
        val result = data as? JsoupPOJO
        result?.isLogin = isLogin(document)
        return parse(document, data)
    }

    fun <T> parse(document: Document, data: T?): T?

}
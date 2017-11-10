package ns.me.ns.furaffinity.datasouce.web.parser

import ns.me.ns.furaffinity.datasouce.web.model.Browse
import org.jsoup.nodes.Document

/**
 * JsoupParser
 */
class BrowseParser : JsoupParser {

    @Suppress("UNCHECKED_CAST")
    override fun <T> parse(document: Document, data: T?): T? {
        val result = data as? Browse ?: return null
        
        val gallery = document.select(".gallery")?.first()
        val figures = gallery?.getElementsByTag("figure")
        figures?.forEach {
            val img = it.getElementsByTag("img")?.first()
            val src = "http:${img?.attr("src")}"
            result.imageSrcs.add(src)
        }
        return result as T
    }

}
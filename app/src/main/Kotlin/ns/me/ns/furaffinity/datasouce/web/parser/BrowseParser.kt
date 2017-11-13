package ns.me.ns.furaffinity.datasouce.web.parser

import ns.me.ns.furaffinity.Constants
import ns.me.ns.furaffinity.datasouce.web.model.Browse
import org.jsoup.nodes.Document

/**
 * [Browse] Parser
 */
class BrowseParser() : JsoupParser<Browse> {

    override val requiredLogin: Boolean = false

    @Suppress("UNCHECKED_CAST")
    override fun parse(document: Document, data: Browse?): Browse? {
        TODO() // UNCHECKED
        val result = data as? Browse ?: return null

        val gallery = document.select("section.gallery")?.first()
        val figures = gallery?.getElementsByTag("figure")
        figures?.forEach {
            val img = it.getElementsByTag("img")?.first()
            val src = "http:${img?.attr("src")}"
            val aHref = it.getElementsByTag("a")?.first()
            val link = "${Constants.WEB_BASE}:${aHref?.attr("href")}"
            result.images.add(Browse.Image(src, link))
        }
        return result
    }

}
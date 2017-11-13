package ns.me.ns.furaffinity.datasouce.web.parser

import ns.me.ns.furaffinity.Constants
import ns.me.ns.furaffinity.datasouce.web.model.MsgSubmissions
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * [MsgSubmissions] Parser
 */
class MsgSubmissionsParser : JsoupParser<MsgSubmissions> {

    override val requiredLogin: Boolean = true

    companion object {
        val pathMoreRegex = "/msg/submissions/(.*)".toRegex()
        val viewIdRegex = "/view/(.*)/".toRegex()
    }

    @Suppress("UNCHECKED_CAST")
    override fun parse(document: Document, data: MsgSubmissions?): MsgSubmissions? {
        val result = data ?: return null

        val gallery = document.select("section.gallery")
        gallery.forEach {
            val figures = it.getElementsByTag("figure")
            figures?.forEach {
                result.images.add(parseFigure(it))
            }
        }


        document.selectFirst("a.more:not(.prev), a.more-half:not(.prev)")?.attr("href")?.let {
            pathMoreRegex.matchEntire(it)?.groups?.let {
                if (it.size == 2) {
                    result.pathMore = it[1]?.value
                }
            }
        }

        return result
    }

    private fun parseFigure(figure: Element): MsgSubmissions.Image {

        val result = MsgSubmissions.Image()

        var link: String? = null
        var viewId: Int? = null
        figure.getElementsByTag("a")?.first()?.attr("href")?.let {
            // link
            link = "${Constants.WEB_BASE}$it"
            // viewId
            viewIdRegex.matchEntire(it)?.groups?.let {
                if (it.size == 2) {
                    viewId = it[1]?.value?.toIntOrNull()
                }
            }
        }


        // title
        val titleTag = figure.getElementsByTag("figcaption")?.first()?.getElementsByTag("a")?.first()
        val title = titleTag?.attr("title")
        // src
        val img = figure.getElementsByTag("img")?.first()
        val src = "http:${img?.attr("src")}"

        return MsgSubmissions.Image(title = title, src = src, viewId = viewId, link = link)
    }

}
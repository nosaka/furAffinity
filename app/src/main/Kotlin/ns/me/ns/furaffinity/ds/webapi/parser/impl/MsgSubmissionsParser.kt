package ns.me.ns.furaffinity.repository.model.remote.parser.impl

import ns.me.ns.furaffinity.repository.model.remote.MsgSubmissions
import ns.me.ns.furaffinity.repository.model.remote.model.entity.ViewElement
import ns.me.ns.furaffinity.repository.model.remote.parser.JsoupParser
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * [MsgSubmissions] Parser
 */
class MsgSubmissionsParser : JsoupParser<MsgSubmissions> {

    override val requiredLogin: Boolean = true

    companion object {
        val pathMoreRegex = "/msg/submissions/(.*)".toRegex()
    }

    @Suppress("UNCHECKED_CAST")
    override fun parse(document: Document, data: MsgSubmissions?): MsgSubmissions? {
        val result = data ?: return null

        val gallery = document.select("section.gallery")
        gallery.forEach {
            val figures = it.getElementsByTag("figure")
            figures?.forEach {
                result.viewElements.add(parseFigure(it))
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

    private fun parseFigure(figure: Element): ViewElement {
        val result = ViewElement()

        figure.getElementsByTag("figcaption")?.first()?.let {
            // view Id
            result.viewId = it.getElementsByTag("input")?.`val`()?.toIntOrNull()
            // name
            val viewTag = it.getElementsByAttributeValueMatching("href", "/view/.*/")
            result.name = viewTag?.attr("title")

            // user
            val userTag = it.getElementsByAttributeValueMatching("href", "/userElement/.*/")
            result.userElement.name = userTag?.attr("title")
            result.userElement.href = userTag?.attr("href")
        }

        figure.getElementsByTag("img")?.first()?.let {
            it.attr("src")?.let {
                result.imageElement.src = "http:$it"
            }
            result.imageElement.alt = it.attr("alt")
        }

        return result
    }

}
package ns.me.ns.furaffinity.ds.webapi.parser.impl

import ns.me.ns.furaffinity.ds.webapi.parser.JsoupParser
import ns.me.ns.furaffinity.repository.model.remote.MsgSubmissions
import ns.me.ns.furaffinity.repository.model.remote.entity.ViewElement
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * [MsgSubmissions] Parser
 */
class MsgSubmissionsParser : JsoupParser<MsgSubmissions> {

    override val requiredLogin: Boolean = true

    companion object {
        val accountRegex = "/user/(.*)/".toRegex()
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

        return result
    }

    private fun parseFigure(figure: Element): ViewElement {
        val result = ViewElement()

        figure.getElementsByTag("figcaption")?.first()?.let {
            // view Id
            result.viewId = it.getElementsByTag("input")?.`val`()?.toIntOrNull()
            // account
            val viewTag = it.getElementsByAttributeValueMatching("href", "/view/.*/")
            result.name = viewTag?.attr("title")

            // user
            val userTags = it.getElementsByAttributeValueMatching("href", "/user/.*/")
            val userTag = userTags?.firstOrNull { it.getElementById("my-username") == null }
            val href = userTag?.attr("href")
            href?.let {
                accountRegex.matchEntire(it)?.groups?.let {
                    if (it.size == 2) {
                        result.userElement.account = it[1]?.value
                    }
                }
            }
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
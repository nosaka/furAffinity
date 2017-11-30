package ns.me.ns.furaffinity.ds.webapi.parser.impl

import ns.me.ns.furaffinity.ds.webapi.parser.JsoupParser
import ns.me.ns.furaffinity.repository.model.remote.Browse
import ns.me.ns.furaffinity.repository.model.remote.entity.ViewElement
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * [Browse] Parser
 */
class BrowseParser : JsoupParser<Browse> {

    override val requiredLogin: Boolean = true

    companion object {
        val accountRegex = "/user/(.*)/".toRegex()
    }

    @Suppress("UNCHECKED_CAST")
    override fun parse(document: Document, data: Browse?): Browse? {
        val result = data ?: return null

        val gallery = document.getElementById("gallery-browse")
        val figures = gallery?.getElementsByTag("figure")
        figures?.forEach {
            result.viewElements.add(parseFigure(it))
        }

        return result
    }

    private fun parseFigure(figure: Element): ViewElement {
        val result = ViewElement()

        // view Id
        figure.id()?.let {
            GalleryParser.viewIdRegex.matchEntire(it)?.groups?.let {
                if (it.size == 2) {
                    result.viewId = it[1]?.value?.toIntOrNull()
                }
            }
        }
        figure.getElementsByAttribute("title").firstOrNull { it.getElementsByAttributeValueMatching("href", "/view/.*/")?.size ?: 0 > 0 }?.let {
            result.name = it.attr("title")
        }

        // img
        val imgTag = figure.getElementsByTag("img").firstOrNull()
        imgTag?.let {
            it.attr("src")?.let {
                result.imageElement.src = "http:$it"
            }
            result.imageElement.alt = it.attr("alt")
        }
        // user
        val userTags = figure.getElementsByAttributeValueMatching("href", "/user/.*/")
        val userTag = userTags?.firstOrNull { it.getElementById("my-username") == null }
        val href = userTag?.attr("href")
        href?.let {
            GalleryParser.accountRegex.matchEntire(it)?.groups?.let {
                if (it.size == 2) {
                    result.userElement.account = it[1]?.value
                }
            }
        }

        return result
    }

}
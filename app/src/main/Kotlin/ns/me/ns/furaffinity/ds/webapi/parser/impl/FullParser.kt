package ns.me.ns.furaffinity.ds.webapi.parser.impl

import ns.me.ns.furaffinity.ds.webapi.parser.JsoupParser
import ns.me.ns.furaffinity.repository.model.remote.Full
import org.jsoup.nodes.Document

/**
 * [Full] Parser
 */
class FullParser : JsoupParser<Full> {

    override val requiredLogin: Boolean = true

    @Suppress("UNCHECKED_CAST")
    override fun parse(document: Document, data: Full?): Full? {
        val result = data ?: return null

        result.title = document.title()
        val submissionImg = document.getElementById("submissionImg")
        submissionImg?.let {
            it.attr("src")?.let {
                result.imageElement.src = "http:$it"
            }
            result.imageElement.alt = it.attr("alt")
        }
        // user
        val userTags = document.getElementsByAttributeValueMatching("href", "/user/.*/")
        val userTag = userTags?.firstOrNull { it.getElementById("my-username") == null }
        result.userElement.name = userTag?.text()
        result.userElement.href = userTag?.attr("href")

        return result
    }


}
package ns.me.ns.furaffinity.datasouce.web.parser

import ns.me.ns.furaffinity.datasouce.web.model.Full
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
            result.alt = it.attr("alt")
            result.src = "http:${it.attr("src")}"
        }

        return result
    }


}
package ns.me.ns.furaffinity.datasouce.web.parser.impl

import ns.me.ns.furaffinity.datasouce.web.model.impl.Full
import ns.me.ns.furaffinity.datasouce.web.parser.JsoupParser
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
        return result
    }


}
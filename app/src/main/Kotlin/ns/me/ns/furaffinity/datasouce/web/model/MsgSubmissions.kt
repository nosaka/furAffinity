package ns.me.ns.furaffinity.datasouce.web.model

/**
 * MsgSubmissions
 */
class MsgSubmissions : JsoupPOJO() {

    val images = ArrayList<Image>()

    var pathMore: String? = null

    data class Image(val title: String? = null,
                     val src: String? = null,
                     val link: String? = null,
                     val viewId: Int? = null)
}
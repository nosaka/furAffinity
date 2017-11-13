package ns.me.ns.furaffinity.datasouce.web.model

/**
 * Browse
 */
class Browse : JsoupPOJO() {

    val images = ArrayList<Image>()

    data class Image(var src: String? = null,
                     var link: String? = null)
}
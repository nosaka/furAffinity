package ns.me.ns.furaffinity.datasouce.web.model.impl

import ns.me.ns.furaffinity.datasouce.web.model.AbstractBaseJsoupResponse
import ns.me.ns.furaffinity.datasouce.web.model.impl.entity.ImageElement

/**
 * Full
 */
class Full : AbstractBaseJsoupResponse() {

    var title: String? = null

    var imageElement: ImageElement = ImageElement()
}
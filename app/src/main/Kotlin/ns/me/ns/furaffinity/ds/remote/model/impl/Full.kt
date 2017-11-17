package ns.me.ns.furaffinity.ds.remote.model.impl

import ns.me.ns.furaffinity.ds.remote.model.AbstractBaseJsoupResponse
import ns.me.ns.furaffinity.ds.remote.model.impl.entity.ImageElement

/**
 * Full
 */
class Full : AbstractBaseJsoupResponse() {

    var title: String? = null

    var imageElement: ImageElement = ImageElement()
}
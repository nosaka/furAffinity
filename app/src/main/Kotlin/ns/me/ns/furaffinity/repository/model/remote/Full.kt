package ns.me.ns.furaffinity.repository.model.remote

import ns.me.ns.furaffinity.repository.model.AbstractBaseJsoupResponse
import ns.me.ns.furaffinity.repository.model.remote.model.entity.ImageElement

/**
 * Full
 */
class Full : AbstractBaseJsoupResponse() {

    var title: String? = null

    var imageElement: ImageElement = ImageElement()
}
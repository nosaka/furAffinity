package ns.me.ns.furaffinity.repository.model.remote

import ns.me.ns.furaffinity.repository.model.AbstractBaseJsoupResponse
import ns.me.ns.furaffinity.repository.model.remote.entity.ImageElement
import ns.me.ns.furaffinity.repository.model.remote.entity.UserElement

/**
 * Full
 */
class Full : AbstractBaseJsoupResponse() {

    var title: String? = null

    var imageElement: ImageElement = ImageElement()

    var userElement: UserElement = UserElement()
}
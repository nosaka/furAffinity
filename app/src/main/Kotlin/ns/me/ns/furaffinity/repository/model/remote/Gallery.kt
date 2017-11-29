package ns.me.ns.furaffinity.repository.model.remote

import ns.me.ns.furaffinity.repository.model.AbstractBaseJsoupResponse
import ns.me.ns.furaffinity.repository.model.remote.entity.ViewElement

/**
 * Gallery
 */
class Gallery : AbstractBaseJsoupResponse() {

    val viewElements = ArrayList<ViewElement>()

}
package ns.me.ns.furaffinity.repository.model.remote

import ns.me.ns.furaffinity.repository.model.AbstractBaseJsoupResponse
import ns.me.ns.furaffinity.repository.model.remote.entity.ViewElement

/**
 * Browse
 */
class Browse : AbstractBaseJsoupResponse() {

    val viewElements = ArrayList<ViewElement>()
}
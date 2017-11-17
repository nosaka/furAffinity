package ns.me.ns.furaffinity.ds.remote.model.impl

import ns.me.ns.furaffinity.ds.remote.model.AbstractBaseJsoupResponse
import ns.me.ns.furaffinity.ds.remote.model.impl.entity.ViewElement

/**
 * MsgSubmissions
 */
class MsgSubmissions : AbstractBaseJsoupResponse() {

    val viewElements = ArrayList<ViewElement>()

    var pathMore: String? = null
}
package ns.me.ns.furaffinity.datasouce.web.model.impl

import ns.me.ns.furaffinity.datasouce.web.model.AbstractBaseJsoupResponse
import ns.me.ns.furaffinity.datasouce.web.model.impl.entity.ViewElement

/**
 * MsgSubmissions
 */
class MsgSubmissions : AbstractBaseJsoupResponse() {

    val viewElements = ArrayList<ViewElement>()

    var pathMore: String? = null
}
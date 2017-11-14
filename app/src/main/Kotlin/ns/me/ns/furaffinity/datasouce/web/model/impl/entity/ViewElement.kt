package ns.me.ns.furaffinity.datasouce.web.model.impl.entity

/**
 *
 */
open class ViewElement(var viewId: Int? = null,
                       var name: String? = null,
                       var href: String? = null,
                       var imageElement: ImageElement = ImageElement(),
                       var userElement: UserElement = UserElement())
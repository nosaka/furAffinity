package ns.me.ns.furaffinity.repository.model.remote.entity

/**
 *
 */
open class ViewElement(var viewId: Int? = null,
                       var name: String? = null,
                       var imageElement: ImageElement = ImageElement(),
                       var userElement: UserElement = UserElement())
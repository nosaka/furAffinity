package ns.me.ns.furaffinity.repository.model

import android.databinding.ObservableField
import ns.me.ns.furaffinity.repository.model.remote.entity.ImageElement
import ns.me.ns.furaffinity.repository.model.remote.entity.UserElement
import ns.me.ns.furaffinity.ui.ObservableDrawableTarget

/**
 * Imageインターフェース
 */
interface ViewInterface {

    var viewId: Int

    var name: String?

    val image: ObservableDrawableTarget
        get() = ObservableDrawableTarget()

    val imageElement: ObservableField<ImageElement>
        get() = ObservableField()

    val userElement: ObservableField<UserElement>
        get() = ObservableField()


}
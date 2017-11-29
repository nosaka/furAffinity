package ns.me.ns.furaffinity.repository.model.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.databinding.ObservableField
import ns.me.ns.furaffinity.repository.model.ViewInterface
import ns.me.ns.furaffinity.repository.model.remote.entity.ImageElement
import ns.me.ns.furaffinity.repository.model.remote.entity.UserElement

/**
 *
 */
@Entity(tableName = "Gallery")
open class Gallery : ViewInterface {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "VIEW_ID", typeAffinity = ColumnInfo.INTEGER)
    override var viewId: Int = 0

    @ColumnInfo(name = "NAME", typeAffinity = ColumnInfo.TEXT)
    override var name: String? = null

    @ColumnInfo(name = "ACCOUNT", typeAffinity = ColumnInfo.TEXT)
    var account: String? = null

    @ColumnInfo(name = "SRC", typeAffinity = ColumnInfo.TEXT)
    var src: String? = null

    @ColumnInfo(name = "ALT", typeAffinity = ColumnInfo.TEXT)
    var alt: String? = null

    override val imageElement: ObservableField<ImageElement>
        get() {
            val value = ObservableField<ImageElement>()
            value.set(ImageElement(src, if (alt?.isNotBlank() == true) alt else name))
            return value
        }

    override val userElement: ObservableField<UserElement>
        get() {
            val value = ObservableField<UserElement>()
            value.set(UserElement(account))
            return value
        }
}
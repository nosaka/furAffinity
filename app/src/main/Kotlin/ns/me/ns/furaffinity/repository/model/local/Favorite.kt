package ns.me.ns.furaffinity.repository.model.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.databinding.ObservableField
import ns.me.ns.furaffinity.repository.model.ViewInterface
import ns.me.ns.furaffinity.repository.model.remote.entity.ImageElement

/**
 *
 */
@Entity(tableName = "Favorite")
open class Favorite : ViewInterface {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "VIEW_ID", typeAffinity = ColumnInfo.INTEGER)
    override var viewId: Int = 0

    @ColumnInfo(name = "SRC", typeAffinity = ColumnInfo.TEXT)
    var src: String? = null

    @ColumnInfo(name = "ALT", typeAffinity = ColumnInfo.TEXT)
    var alt: String? = null

    @ColumnInfo(name = "IMAGE_DATA", typeAffinity = ColumnInfo.BLOB)
    var imageData: ByteArray? = null

    override val imageElement: ObservableField<ImageElement>
        get() {
            val value = ObservableField<ImageElement>()
            value.set(ImageElement(src, alt))
            return value
        }

}
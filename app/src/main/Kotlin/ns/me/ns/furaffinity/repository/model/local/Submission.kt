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
@Entity(tableName = "Submission")
open class Submission : ViewInterface {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "VIEW_ID", typeAffinity = ColumnInfo.INTEGER)
    override var viewId: Int = 0

    @ColumnInfo(name = "SRC", typeAffinity = ColumnInfo.TEXT)
    var src: String? = null

    @ColumnInfo(name = "ALT", typeAffinity = ColumnInfo.TEXT)
    var alt: String? = null

    @ColumnInfo(name = "NAME", typeAffinity = ColumnInfo.TEXT)
    var name: String? = null

    override val imageElement: ObservableField<ImageElement>
        get() {
            val value = ObservableField<ImageElement>()
            value.set(ImageElement(src, if (alt?.isNotBlank() == true) alt else name))
            return value
        }
}
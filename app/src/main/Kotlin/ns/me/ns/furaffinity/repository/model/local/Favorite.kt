package ns.me.ns.furaffinity.repository.model.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import ns.me.ns.furaffinity.repository.model.ViewImageInterface

/**
 *
 */
@Entity(tableName = "Favorite")
open class Favorite : ViewImageInterface {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "VIEW_ID", typeAffinity = ColumnInfo.INTEGER)
    override var viewId: Int = 0

    @ColumnInfo(name = "SRC", typeAffinity = ColumnInfo.TEXT)
    override var src: String? = null

    @ColumnInfo(name = "ALT", typeAffinity = ColumnInfo.TEXT)
    override var alt: String? = null

    @ColumnInfo(name = "IMAGE_DATA", typeAffinity = ColumnInfo.BLOB)
    var imageData: ByteArray? = null
}
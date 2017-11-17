package ns.me.ns.furaffinity.ds.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 *
 */
@Entity(tableName = "Favorite")
open class Favorite {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "VIEW_ID", typeAffinity = ColumnInfo.INTEGER)
    open var viewId: Int = 0

    @ColumnInfo(name = "SRC", typeAffinity = ColumnInfo.TEXT)
    open var src: String? = null

    @ColumnInfo(name = "IMAGE_DATA", typeAffinity = ColumnInfo.BLOB)
    var imageData: ByteArray? = null

    @ColumnInfo(name = "ALT", typeAffinity = ColumnInfo.TEXT)
    open var alt: String? = null
}
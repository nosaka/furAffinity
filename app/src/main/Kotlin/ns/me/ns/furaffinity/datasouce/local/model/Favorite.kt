package ns.me.ns.furaffinity.datasouce.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 *
 */
@Entity(tableName = "Favorite")
class Favorite {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "VIEW_ID", typeAffinity = ColumnInfo.INTEGER)
    var viewId: Int = 0

    @ColumnInfo(name = "SRC", typeAffinity = ColumnInfo.TEXT)
    var src: String? = null

    @ColumnInfo(name = "SRC", typeAffinity = ColumnInfo.BLOB)
    var imageData: ByteArray? = null

    @ColumnInfo(name = "ALT", typeAffinity = ColumnInfo.TEXT)
    var alt: String? = null
}
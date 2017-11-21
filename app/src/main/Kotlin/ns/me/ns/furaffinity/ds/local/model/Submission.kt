package ns.me.ns.furaffinity.ds.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 *
 */
@Entity(tableName = "Submission")
open class Submission {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "VIEW_ID", typeAffinity = ColumnInfo.INTEGER)
    open var viewId: Int = 0

    @ColumnInfo(name = "NAME", typeAffinity = ColumnInfo.TEXT)
    open var name: String? = null

    @ColumnInfo(name = "SRC", typeAffinity = ColumnInfo.TEXT)
    open var src: String? = null

    @ColumnInfo(name = "ALT", typeAffinity = ColumnInfo.TEXT)
    open var alt: String? = null
}
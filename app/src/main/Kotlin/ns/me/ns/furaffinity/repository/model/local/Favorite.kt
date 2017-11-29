package ns.me.ns.furaffinity.repository.model.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.databinding.ObservableField
import android.graphics.drawable.BitmapDrawable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ns.me.ns.furaffinity.repository.model.ViewInterface
import ns.me.ns.furaffinity.repository.model.remote.entity.ImageElement
import ns.me.ns.furaffinity.repository.model.remote.entity.UserElement
import ns.me.ns.furaffinity.ui.ObservableDrawableTarget
import ns.me.ns.furaffinity.util.BitmapUtil
import ns.me.ns.furaffinity.util.LogUtil

/**
 *
 */
@Entity(tableName = "Favorite")
open class Favorite : ViewInterface {

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

    @ColumnInfo(name = "IMAGE_DATA", typeAffinity = ColumnInfo.BLOB)
    var imageData: ByteArray? = null

    override val image: ObservableDrawableTarget
        get() = ObservableDrawableTarget().apply {
            imageData?.let {
                BitmapUtil.getBitmap(it)
                        .observeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            set(BitmapDrawable(null, it))
                        }, {
                            LogUtil.e(it)
                        })
            }
        }

    override val imageElement: ObservableField<ImageElement>
        get() {
            val value = ObservableField<ImageElement>()
            value.set(ImageElement(src, alt))
            return value
        }

    override val userElement: ObservableField<UserElement>
        get() {
            val value = ObservableField<UserElement>()
            value.set(UserElement(account))
            return value
        }

}
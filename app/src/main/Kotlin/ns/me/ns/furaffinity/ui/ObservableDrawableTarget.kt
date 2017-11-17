package ns.me.ns.furaffinity.ui

import android.databinding.ObservableField
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

/**
 * ObservableDrawableTarget
 */
class ObservableDrawableTarget : ObservableField<Drawable>(), Target {

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        set(placeHolderDrawable)
    }

    override fun onBitmapFailed(errorDrawable: Drawable?) {
        set(errorDrawable)
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        set(BitmapDrawable(null, bitmap))
    }
}
package ns.me.ns.furaffinity.util

import android.graphics.*
import com.squareup.picasso.Transformation


/**
 *
 */
class RoundedTransformation : Transformation {

    override fun key(): String {
        return "DUMMT"

    }

    override fun transform(source: Bitmap?): Bitmap? {
        if (source == null || source.isRecycled) {
            return source
        }

        val width = source.width
        val height = source.height

        val paint = Paint()
        paint.isAntiAlias = true;
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        val canvas = Canvas(output)

        val radius = if (width > height) height / 2f else width / 2f
        canvas.drawRoundRect(RectF(0f, 0f, width.toFloat(), height.toFloat()), radius, radius, paint)

        if (source != output) {
            source.recycle();
        }

        return output;

    }

}
package ns.me.ns.furaffinity.util


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import io.reactivex.Completable
import io.reactivex.Single
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream


/**
 * Bitmap画像操作Util
 */
object BitmapUtil {

    /**
     * ファイルパスからBitmapを取得
     *
     * @param path 画像ファイルパス
     * @return 正常に画像が生成できれば画像データ, 画像の生成に失敗したらnullが返却されます
     */
    fun getBitmap(path: String): Single<Bitmap> {
        return Single.create<Bitmap> {
            try {
                val bitmap = BitmapFactory.decodeFile(path)
                it.onSuccess(bitmap)
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    /**
     * ファイルパスからBitmapを取得
     *
     * @param uri 画像ファイルUri
     * @return 正常に画像が生成できれば画像データ, 画像の生成に失敗したらnullが返却されます
     */
    fun getBitmap(uri: Uri): Single<Bitmap> {
        return getBitmap(uri.path)
    }

    /**
     * バイト列からBitmapを取得
     *
     * @param data 画像バイト列
     * @return 正常に画像が生成できれば画像データ、画像の生成に失敗したらnullが返却されます
     */
    fun getBitmap(data: ByteArray): Single<Bitmap> {
        return Single.create<Bitmap> {
            try {
                val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                it.onSuccess(bitmap)
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    /**
     * ストリームからBitmapを取得します。
     *
     * @param is      画像データの入力ストリーム
     * @param options 画像を生成する際に適用するオプション
     * @return 正常に画像が生成できれば画像データ、画像の生成に失敗したらnullが返却されます
     */
    fun getBitmap(`is`: InputStream, options: Options): Single<Bitmap> {
        return Single.create<Bitmap> {
            try {
                val bitmap = BitmapFactory.decodeStream(`is`, null, options)
                it.onSuccess(bitmap)
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    /**
     * ストリームからBitmapを取得
     *
     * @param is 画像データの入力ストリーム
     * @param width  画像データの幅
     * @param height 画像データの高さ
     * @return 正常に画像が生成できれば画像データ、画像の生成に失敗したらnullが返却されます
     */
    fun getBitmap(`is`: InputStream, width: Int? = null, height: Int? = null): Single<Bitmap> {
        val options = Options()
        if (width != null && height != null) {
            options.outWidth = width
            options.outHeight = height
        }

        return getBitmap(`is`, options)
    }

    /**
     * 画像スケールリサイズ
     *
     * @param orgBitmap 元画像
     * @param newWidth  指定幅
     * @param newHeight 指定高
     * @return リサイズ後画像
     */
    fun resizeScale(orgBitmap: Bitmap, newWidth: Int, newHeight: Int): Single<Bitmap> {

        return Single.create<Bitmap> {

            val bitmapHeight = orgBitmap.height
            val bitmapWidth = orgBitmap.width

            // 以下の通りに[高]、[幅]拡大スケールを算出
            // ・指定[高] / 指定[高]
            // ・指定[幅] / 画像[幅]
            var resizeScaleHeight = (newHeight / bitmapHeight).toFloat()
            var resizeScaleWidth = (newWidth / bitmapWidth).toFloat()

            if (resizeScaleHeight >= resizeScaleWidth) {
                // [高]拡大スケールの方が[幅]拡大スケールより大きい場合は以下のケースが想定される
                // ・指定[高]が大きい場合
                // ・画像[幅]が大きい場合
                // この場合、[高]拡大スケールを基準とし、期待値は画像の[高]がぴったり収まり、[幅]が一部隠れる結果となる
                resizeScaleHeight = (newHeight / bitmapHeight).toFloat()
                resizeScaleWidth = (newHeight / bitmapHeight).toFloat()
            } else {
                // [幅]拡大スケールの方が[高]拡大スケールより大きい場合は以下のケースが想定される
                // ・指定[幅]が大きい場合
                // ・画像[高]が大きい場合
                // この場合、[幅]拡大スケールを基準とし、期待値は画像の[幅]がぴったり収まり、[高]が一部隠れる結果となる
                resizeScaleHeight = (newWidth / bitmapWidth).toFloat()
                resizeScaleWidth = (newWidth / bitmapWidth).toFloat()
            }
            // スケール値から画像を再作成
            val matrix = Matrix()
            matrix.postScale(resizeScaleWidth, resizeScaleHeight)
            val resizeBitmap = Bitmap.createBitmap(
                    orgBitmap,
                    0, 0,
                    orgBitmap.width, orgBitmap.height,
                    matrix, true)

            LogUtil.d("resize scale width:=" + java.lang.Float.toString(resizeScaleWidth))
            LogUtil.d("resize scale height:=" + java.lang.Float.toString(resizeScaleHeight))
            LogUtil.d("org bitmap width:=" + Integer.toString(bitmapWidth))
            LogUtil.d("org bitmap height:=" + Integer.toString(bitmapHeight))
            LogUtil.d("resize bitmap width:=" + Integer.toString(resizeBitmap.width))
            LogUtil.d("resize bitmap height:=" + Integer.toString(resizeBitmap.height))

            it.onSuccess(resizeBitmap)
        }

    }

    /**
     * ビットマップをファイル出力
     *
     * @param context  [Context]
     * @param fileName ファイル名
     * @param bitmap   [Bitmap]
     * @param format [Bitmap.CompressFormat]
     * @param quality  品質。0-100で指定する
     */
    fun write(fileName: String,
              bitmap: Bitmap,
              format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
              quality: Int = 100): Completable {

        return Completable.fromAction {
            Environment.getExternalStorageDirectory()?.let {
                val file = File(it, "$fileName.png")
                FileOutputStream(file).use {
                    bitmap.compress(format, quality, it)
                    it.flush()
                }
            }
        }
    }

    /**
     * [Options]取得
     *
     * @param is 画像データの入力ストリーム
     * @return [Options]
     * @throws FileNotFoundException
     */
    @Throws(FileNotFoundException::class)
    fun getOptions(`is`: InputStream): Options {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(`is`, null, options)
        return options
    }

    /**
     * サンプルサイズ計算
     *
     * @param options   [Options]
     * @param reqWidth  要求幅
     * @param reqHeight 要求高
     */
    fun calculateInSampleSize(options: Options, reqWidth: Int, reqHeight: Int) {
        var inSampleSize = 1
        if (options.outHeight > reqHeight || options.outWidth > reqWidth) {
            inSampleSize = if (options.outWidth > options.outHeight) {
                Math.round(options.outHeight.toFloat() / reqHeight.toFloat())
            } else {
                Math.round(options.outWidth.toFloat() / reqWidth.toFloat())
            }
        }
        options.inSampleSize = inSampleSize
    }


}
package ns.me.ns.furaffinity.util

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast

import java.lang.ref.SoftReference

/**
 * トースト表示用Utility
 *
 */
object ToastUtil {

    /**
     * 表示するトーストを生成・更新するリスナー。
     *
     * @author shintaro-nosaka
     */
    interface OnCreateToastListener {

        fun onCreateToast(context: Context, message: String): Toast
    }

    /** トースト保持用弱参照リファレンス  */
    private var sToastRef: SoftReference<Toast>? = null

    /** トースト表示時間  */
    private var lastShowToast: Long = 0

    /**
     * 現在表示中のトーストが存在すれば破棄して、指定されたトーストを表示します。
     *
     *
     * 連続トースト表示に対応しています。
     *
     * @param context       コンテキスト
     * @param messageId     表示するメッセージのリソースID
     */
    fun showToast(
            context: Context,
            messageId: Int) {
        showToast(context, context.getString(messageId), null)
    }

    /**
     * 現在表示中のトーストが存在すれば破棄して、指定されたトーストを表示します。
     *
     *
     * 連続トースト表示に対応しています。
     *
     * @param context       コンテキスト
     * @param messageId     表示するメッセージのリソースID
     * @param messageParams メッセージリソースパラム
     */
    fun showToast(
            context: Context,
            messageId: Int,
            vararg messageParams: Any) {
        showToast(context, context.getString(messageId, *messageParams), null)
    }

    /**
     * 現在表示中のトーストが存在すれば破棄して、指定されたトーストを表示します。
     *
     *
     * 連続トースト表示に対応しています。
     *
     * @param context       コンテキスト
     * @param messageId     表示するメッセージのリソースID
     * @param messageParams メッセージリソースパラム
     */
    fun showToast(
            context: Context,
            messageId: Int,
            duration: Int,
            vararg messageParams: Any) {
        showToast(context, context.getString(messageId, *messageParams), duration)
    }

    /**
     * 現在表示中のトーストが存在すれば破棄して、指定されたトーストを表示します。
     *
     *
     * 連続トースト表示に対応しています。<br></br>
     * リスナーを指定した場合、トーストが生成された段階でコールバックメソッドが呼ばれます。
     * このコールバックの中では表示するトーストを生成することを期待します。
     *
     * @param context   コンテキスト
     * @param messageId 表示するメッセージのリソースID
     * @param listener  トースト生成・更新するリスナー
     */
    fun showToast(context: Context, messageId: Int,
                  listener: OnCreateToastListener) {
        showToast(context, context.getString(messageId), listener)
    }

    /**
     * 現在表示中のトーストが存在すれば破棄して、指定されたトーストを表示します。
     *
     *
     * 連続トースト表示に対応しています。
     *
     * @param context コンテキスト
     * @param message 表示するメッセージ
     */
    @Synchronized
    fun showToast(context: Context,
                  message: String) {
        showToast(context, message, null)
    }

    /**
     * 現在表示中のトーストが存在すれば破棄して、指定されたトーストを表示します。
     *
     *
     * 連続トースト表示に対応しています。
     *
     * @param context コンテキスト
     * @param message 表示するメッセージ
     */
    @SuppressLint("ShowToast")
    @Synchronized
    fun showToast(context: Context,
                  message: String, duration: Int) {
        showToast(context, message, object : OnCreateToastListener {
            override fun onCreateToast(context: Context, message: String): Toast = Toast.makeText(context, message, duration)
        })
    }

    /**
     * 現在表示中のトーストが存在すれば破棄して、指定されたトーストを表示します。
     *
     *
     * 連続トースト表示に対応しています。 リスナーを指定した場合、トーストが生成された段階でコールバックメソッドが呼ばれます。
     * このコールバックの中では表示するトーストを生成することを期待します。
     *
     * @param context  コンテキスト
     * @param message  表示するメッセージ
     * @param listener トースト生成・更新するリスナー
     */
    @Synchronized
    fun showToast(context: Context,
                  message: String, listener: OnCreateToastListener?) {
        val ref = sToastRef
        if (ref != null) {
            val toast = ref.get()
            if (toast != null && System.currentTimeMillis() < lastShowToast) {
                toast.setText(message)
                toast.show()
                updateLastShowToast(toast)
                return
            }
        }

        val toast: Toast
        if (listener != null) {
            toast = listener.onCreateToast(context, message)
        } else {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        }
        toast.show()
        updateLastShowToast(toast)
        sToastRef = SoftReference(toast)
    }

    private fun updateLastShowToast(toast: Toast) {
        val duration = toast.duration
        if (Toast.LENGTH_SHORT == duration) {
            lastShowToast = System.currentTimeMillis() + 3000 // 3sec
        } else {
            lastShowToast = System.currentTimeMillis() + 5000 // 5sec
        }
    }

}

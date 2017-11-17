package ns.me.ns.furaffinity.ds.remote

import android.content.Context
import ns.me.ns.furaffinity.Constants
import ns.me.ns.furaffinity.util.LoginManager
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


/**
 * AppWebApiClient
 */
class AppWebApiClient {

    companion object {

        private val interceptor = Interceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder().build()

            chain.proceed(request)
        }

        private fun client(context: Context) = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cookieJar(AppWebCookieJar(context))
                .build()


        private fun retrofit(context: Context) = Retrofit.Builder()
                .baseUrl(Constants.WEB_BASE)
                .client(client(context))
                .addConverterFactory(JsoupConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        fun service(context: Context): AppWebApiService =
                retrofit(context).create(AppWebApiService::class.java)


    }

    data class AppWebCookieJar(private val context: Context) : CookieJar {

        override fun saveFromResponse(url: HttpUrl?, cookies: MutableList<Cookie>?) {
            // 処理なし
        }

        override fun loadForRequest(url: HttpUrl?): List<Cookie>? {
            return LoginManager.instance.getCookie(context)
        }
    }

}
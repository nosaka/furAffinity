package ns.me.ns.furaffinity.datasouce.web

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


/**
 * FurAffinityWebApiClient
 */
class FurAffinityWebApiClient {
    companion object {

        private val interceptor = Interceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder().build()

            chain.proceed(request)
        }

        private val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()


        private val retrofit = Retrofit.Builder()
                .baseUrl("https://www.furaffinity.net/")
                .client(client)
                .addConverterFactory(JsoupConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        val service: FurAffinityWebApiService = retrofit.create(FurAffinityWebApiService::class.java)


    }


}
package ns.me.ns.furaffinity.ds.webapi

import io.reactivex.Single
import ns.me.ns.furaffinity.repository.model.remote.Full
import ns.me.ns.furaffinity.repository.model.remote.MsgSubmissions
import ns.me.ns.furaffinity.repository.model.remote.parser.impl.FullParser
import ns.me.ns.furaffinity.repository.model.remote.parser.impl.MsgSubmissionsParser
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * AppWebApiService
 */
interface AppWebApiService {

    @GET("/msg/submissions/new~{nextViewId}@72")
    @JsoupParserType(MsgSubmissionsParser::class)
    fun getMsgSubmissions(@Path("nextViewId") viewId: Int = 0): Single<MsgSubmissions>

    @GET("/full/{viewId}/")
    @JsoupParserType(FullParser::class)
    fun getFull(@Path("viewId") viewId: Int): Single<Full>
}
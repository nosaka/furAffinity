package ns.me.ns.furaffinity.ds.remote

import io.reactivex.Observable
import ns.me.ns.furaffinity.ds.remote.model.impl.Full
import ns.me.ns.furaffinity.ds.remote.model.impl.MsgSubmissions
import ns.me.ns.furaffinity.ds.remote.parser.impl.FullParser
import ns.me.ns.furaffinity.ds.remote.parser.impl.MsgSubmissionsParser
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * AppWebApiService
 */
interface AppWebApiService {

    @GET("/msg/submissions/{pathMore}")
    @JsoupParserType(MsgSubmissionsParser::class)
    fun getMsgSubmissions(@Path("pathMore") pathMore: String = ""): Observable<MsgSubmissions>

    @GET("/full/{viewId}/")
    @JsoupParserType(FullParser::class)
    fun getFull(@Path("viewId") viewId: Int): Observable<Full>
}
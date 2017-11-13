package ns.me.ns.furaffinity.datasouce.web

import io.reactivex.Observable
import ns.me.ns.furaffinity.datasouce.web.model.Browse
import ns.me.ns.furaffinity.datasouce.web.model.Full
import ns.me.ns.furaffinity.datasouce.web.model.MsgSubmissions
import ns.me.ns.furaffinity.datasouce.web.parser.BrowseParser
import ns.me.ns.furaffinity.datasouce.web.parser.FullParser
import ns.me.ns.furaffinity.datasouce.web.parser.MsgSubmissionsParser
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * AppWebApiService
 */
interface AppWebApiService {

    @GET("/browse/{page}")
    @JsoupParserType(BrowseParser::class)
    fun getBrowse(@Path("page") page: Int = 0,
                  @Query("cat") cat: String? = null): Observable<Browse>

    @GET("/msg/submissions/{pathMore}")
    @JsoupParserType(MsgSubmissionsParser::class)
    fun getMsgSubmissions(@Path("pathMore") pathMore: String = ""): Observable<MsgSubmissions>

    @GET("/full/{viewId}/")
    @JsoupParserType(FullParser::class)
    fun getFull(@Path("viewId") viewId: Int): Observable<Full>
}
package ns.me.ns.furaffinity.datasouce.web

import io.reactivex.Observable
import ns.me.ns.furaffinity.datasouce.web.model.Browse
import ns.me.ns.furaffinity.datasouce.web.parser.BrowseParser
import ns.me.ns.furaffinity.datasouce.web.parser.JsoupParserType
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * FurAffinityWebApiService
 */
interface FurAffinityWebApiService {

    @GET("/browse/{page}")
    @JsoupParserType(BrowseParser::class)
    fun getBrowse(@Path("page") page: Int = 0, @Query("cat") cat: String? = null): Observable<Browse>
}
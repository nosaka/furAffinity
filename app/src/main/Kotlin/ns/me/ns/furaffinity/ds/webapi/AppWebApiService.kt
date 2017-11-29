package ns.me.ns.furaffinity.ds.webapi

import io.reactivex.Single
import ns.me.ns.furaffinity.ds.webapi.parser.impl.FullParser
import ns.me.ns.furaffinity.ds.webapi.parser.impl.GalleryParser
import ns.me.ns.furaffinity.ds.webapi.parser.impl.MsgSubmissionsParser
import ns.me.ns.furaffinity.repository.model.remote.Full
import ns.me.ns.furaffinity.repository.model.remote.Gallery
import ns.me.ns.furaffinity.repository.model.remote.MsgSubmissions
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

    @GET("/gallery/{account}/{page}/")
    @JsoupParserType(GalleryParser::class)
    fun getGallery(@Path("account") account: String, @Path("page") page: Int = 1): Single<Gallery>

}
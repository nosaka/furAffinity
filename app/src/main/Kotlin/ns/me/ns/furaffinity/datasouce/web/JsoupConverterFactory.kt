package ns.me.ns.furaffinity.datasouce.web

import ns.me.ns.furaffinity.datasouce.web.parser.JsoupParser
import ns.me.ns.furaffinity.datasouce.web.parser.JsoupParserType
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import kotlin.reflect.full.primaryConstructor

/**
 * JsoupConverter
 */
class JsoupConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        val parserType = annotations?.first { it is JsoupParserType }
        val parser = (parserType as? JsoupParserType)?.parser
        val jsoupParser = parser?.primaryConstructor?.call() as? JsoupParser ?: throw IllegalArgumentException("${JsoupParserType::class.java.simpleName} is required.")

        return JsoupConverter(getRawType(type), jsoupParser)
    }
}
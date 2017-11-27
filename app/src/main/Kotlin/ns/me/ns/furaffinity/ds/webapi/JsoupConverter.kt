package ns.me.ns.furaffinity.ds.webapi

import com.google.gson.internal.ObjectConstructor
import ns.me.ns.furaffinity.repository.model.remote.parser.JsoupParser
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Converter
import java.io.IOException
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException

/**
 * JsoupConverter
 */
class JsoupConverter<T>(clazz: Class<T>, jsoupParser: JsoupParser<*>) : Converter<ResponseBody, T> {

    private val constructor: ObjectConstructor<T>? = newDefaultConstructor(clazz)

    @Suppress("UNCHECKED_CAST")
    private val parser = jsoupParser as JsoupParser<T>

    @Throws(IOException::class)
    override fun convert(responseBody: ResponseBody): T? {
        val document = Jsoup.parseBodyFragment(responseBody.string())
        val result = constructor?.construct()
        return parser.parseDocument(document, result)
    }

    private fun <T> newDefaultConstructor(rawType: Class<in T>): ObjectConstructor<T>? {
        try {
            val constructor: Constructor<in T> = rawType.getDeclaredConstructor()
            if (!constructor.isAccessible) {
                constructor.isAccessible = true
            }
            return ObjectConstructor<T> {
                try {
                    @Suppress("UNCHECKED_CAST")
                    return@ObjectConstructor constructor.newInstance() as T
                } catch (e: InstantiationException) {
                    throw RuntimeException("Failed to invoke $constructor with no args", e)
                } catch (e: InvocationTargetException) {
                    throw RuntimeException("Failed to invoke $constructor with no args",
                            e.targetException)
                } catch (e: IllegalAccessException) {
                    throw AssertionError(e)
                }
            }
        } catch (e: NoSuchMethodException) {
            return null
        }

    }

}
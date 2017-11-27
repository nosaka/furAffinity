package ns.me.ns.furaffinity.ds.webapi

import kotlin.reflect.KClass

/**
 * JsoupParserType
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class JsoupParserType(val parser: KClass<*>)
package ns.me.ns.furaffinity.datasouce.web.parser

import kotlin.reflect.KClass

/**
 * JsoupParserType
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class JsoupParserType(val parser: KClass<*>)
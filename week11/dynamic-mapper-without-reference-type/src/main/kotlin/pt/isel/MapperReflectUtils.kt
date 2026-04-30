package pt.isel

import kotlin.collections.component1
import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * 4th version - NaiveMapper enhanced with a cache of parser functions of given a property value
 * and a NaiveMapper fabric.
 */
object MapperReflectUtils {

    fun <T : Any, R : Any> findConstructor(
        src: KClass<T>,
        dest: KClass<R>,
    ): KFunction<R> =
        dest
            .constructors
            .first {
                it
                    .parameters
                    .filter { param -> !param.isOptional }
                    .all { param ->
                        src
                            .memberProperties
                            .any { prop -> matchPropWithParam(prop, param) }
                    }
            }

    /**
     * Selects the constructor with all mandatory parameters
     * in the properties of the source.
     */
    fun <T : Any, R : Any> findMatchingProperties(
        src: KClass<T>,
        destInit: KFunction<R>,
    ): Map<KParameter, KProperty<*>> =
        destInit
            .parameters
            .associateWith { param -> src
                .memberProperties
                .firstOrNull { matchPropWithParam(it, param) }
            }
            .filter { it.value != null } as Map<KParameter, KProperty<*>>



    fun matchPropWithParam(srcProp: KProperty<*>, param: KParameter) : Boolean {
        val fromName = srcProp.findAnnotation<MapProp>()?.paramName ?: srcProp.name
        if(fromName != param.name) {
            return false
        }
        val srcKlass = srcProp.returnType.classifier as KClass<*>
        return if(srcKlass.javaPrimitiveType != null)
            srcProp.returnType == param.type
        else
            true
    }
}
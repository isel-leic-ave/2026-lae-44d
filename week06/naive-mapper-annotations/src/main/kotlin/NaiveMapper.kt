package pt.isel

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * Version 3 of NaiveMapper with a NaiveMapper Class definition
 * Restriction:
 * - only properties with the same name and type.
 */
class NaiveMapper<T : Any>(val srcClass: KClass<*>, val destClass:KClass<T>) {
    /**
     * 1st - selects the constructor with all mandatory parameters
     * in the properties of the source.
     */
    private val destCtor = destClass.constructors
        .first { ctor ->
            ctor.parameters
                .filter { !it.isOptional }
                .all { param ->
                    srcClass.memberProperties
                        .any { matchPropWithParam(it, param) }
                }
        }
    /**
     * 2nd - look for matching properties with ctor parameters.
     * The map relates parameters and properties with the same
     * name and type.
     */
    private val args: Map<KParameter, KProperty<*>?> = destCtor.parameters
        .associateWith { param ->
            srcClass.memberProperties
                .firstOrNull{ matchPropWithParam(it, param) }
        }
        .filter { it.value != null }

    // Function to match the property and parameter names considering annotations.
    private fun matchPropWithParam(srcProp: KProperty<*>, param: KParameter) : Boolean {
        if(srcProp.name == param.name) {
            return srcProp.returnType == param.type
        }
        val annot = srcProp.findAnnotation<MapProp>() ?: return false
        return annot.paramName == param.name && srcProp.returnType == param.type
    }

    /**
     * 3rd - Get the values of properties from source and pass them
     * to te constructor through the callBy()
     */
    fun mapFrom(src: Any) : T {
        val args: Map<KParameter, Any?> = args
            .map { pair -> pair.key to pair.value?.call(src) }
            .associate { it }
        return destCtor.callBy(args)
    }
}
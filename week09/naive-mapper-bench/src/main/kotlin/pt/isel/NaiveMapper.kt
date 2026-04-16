package pt.isel

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * 4rd version - recursive mapper and generics
 */
class NaiveMapper<T : Any>(val srcType: KClass<*>, val destType:KClass<T>) {
    /**
     * Selects the constructor with all mandatory parameters
     * in the properties of the source.
     */
    private val destCtor = destType
        .constructors
        .first { ctor ->
            ctor
                .parameters
                .filter { !it.isOptional }
                .all { param -> srcType
                    .memberProperties
                    .any { matchPropWithParam(it, param) }
                }
        }
    /**
     * Look for matching properties with ctor parameters
     */
    private val args: Map<KParameter, KProperty<*>?> = destCtor
        .parameters
        .associateWith { param -> srcType
            .memberProperties
            .firstOrNull { matchPropWithParam(it, param) }
        }
        .filter { it.value != null }

    /**
     * Match property and parameter names considering reference types
     */
    private fun matchPropWithParam(srcProp: KProperty<*>, param: KParameter) : Boolean {
        // 1st, verify the name considering the annotation.
        val fromName = srcProp.findAnnotation<MapProp>()?.paramName ?: srcProp.name
        if(fromName != param.name) {
            return false
        }
        val srcKlass = srcProp.returnType.classifier as KClass<*>
        //println("${srcKlass.simpleName} ${srcKlass.java.isPrimitive}")
        return if (srcKlass.java.isPrimitive)
            srcProp.returnType == param.type
        else
            true // Default case (e.g., reference type)
    }

    /**
     * Get the values of properties from source and pass them
     * to te constructor through the callBy()
     */
    fun mapFrom(src: Any) : T {
        val arguments: Map<KParameter, Any?> = args
            .map { pair ->
                val propVal = pair.value?.call(src)
                pair.key to parse(pair.value!!.returnType, pair.key.type, propVal)
            }
            .associate { it }
        return destCtor.callBy(arguments)
    }

    /**
     * Property parser considering:
     *     - primitive and String values
     *     - Objects non-primitives (recursively)
     *     - Lists (recursively)
     */
    private fun parse(srcType: KType, destType: KType, propVal: Any?): Any? {
        val srcKlass = srcType.classifier as KClass<*>
        val destKlass = destType.classifier as KClass<*>

        // Primitive or String case
        if(srcKlass.java.isPrimitive || srcKlass == String::class) {
            return propVal
        }

        // List case: use of generics
        if(srcKlass == List::class && destKlass == List::class) {
            // srcType and destType are Ktype.
            // arguments is a property of Ktype that returns a list of Ktype projections.
            // For instance, for List<Song>, Song is the type referred in the arguments list.
            // srcType.arguments[0].type?.classifier returns the class that represents the first type (e.g., Song).
            val elemSrcKlass = srcType.arguments[0].type?.classifier as KClass<*>
            val elemDestKlass = destType.arguments[0].type?.classifier as KClass<*>
            val mapper: NaiveMapper<*> = NaiveMapper(elemSrcKlass, elemDestKlass)

            return (propVal as List<*>).map {
                it?.let { mapper.mapFrom(it) }
                //if (it == null) null else mapper.mapFrom(it)
            }
        }

        // Reference case: with recursion
        val mapper: NaiveMapper<*> = NaiveMapper(srcKlass, destKlass)
        return propVal?.let { mapper.mapFrom(it) }
        //return if(propVal == null) null else mapper.mapFrom(propVal)
        // TODO: Other cases (e.g., Array, Maps, Enum) need to be implemented
    }
}
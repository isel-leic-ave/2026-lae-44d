package pt.isel

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties

/**
 * Version 2.1 of NaiveMapper with a NaiveMapper Class definition
 * Restriction:
 * - only properties with the same name and type.
 */
class NaiveMapper<T : Any>(val srcType: KClass<*>, val destType:KClass<T>) {
    /**
     * 1st - selects the constructor with all mandatory parameters
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
                    .any { it.name == param.name && it.returnType == param.type}
                }
        }
    /**
     * 2nd - look for matching properties with ctor parameters
     */
    private val args: Map<KParameter, KProperty<*>?> = destCtor
        .parameters
        .associateWith { param -> srcType
            .memberProperties
            .firstOrNull() { it.name == param.name && it.returnType == param.type }
        }
        .filter { it.value != null }
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
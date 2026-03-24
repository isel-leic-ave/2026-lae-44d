package pt.isel

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties

/**
 * Version 2.0 of NaiveMapper (function)
 * Copy properties to the parameters of the constructor of dest.
 * It selects the constructor with all mandatory parameters
 * in the properties of the source.
 * Restriction:
 *  - only properties with the same name and type.
 */
fun <T : Any> Any.mapTo(dest: KClass<T>) : T {
    /**
     * 1st - selects the constructor with all mandatory parameters
     * in the properties of the source.
     */
    val destCtor = dest.constructors
        .first { ctor ->
            ctor.parameters
                .filter { !it.isOptional } // only mandatory parameters
                .all { param -> // All parameters with same properties in the source
                    val srcProps = this::class.memberProperties
                    // Any property with same name and type
                    srcProps.any { it.name == param.name && it.returnType == param.type }
                }
        }

    // destCtor.parameters.forEach { println("${it.name}: ${it.type}") } // logger
    /**
     * 2nd - look for matching properties with constructor parameters
     * and get the source object value.
     * callBy needs a Map<KParameter, Any?> to be called.
     */
    val args: Map<KParameter, Any?> = destCtor.parameters
        .associateWith { param ->
            this::class.memberProperties
                .firstOrNull { it.name == param.name && it.returnType == param.type }
                ?.call(this)
        }
        .filter { it.value != null }

    // args.forEach { (k, v) -> println("${k.name} -> $v") } // logger
    /**
     * 3rd create the instance of dest via constructor using
     * the callBy<Map<KParameter, Any>>
     */
    return destCtor.callBy(args)
}
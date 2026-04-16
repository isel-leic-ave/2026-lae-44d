package pt.isel

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * Version 2.2 of NaiveMapper (function)
 * Copy properties to the parameters of the constructor of dest.
 * It selects the constructor with all mandatory parameters
 * in the properties of the source.
 *
 */
fun <T : Any> Any.mapTo(dest: KClass<T>) : T {
    val src = this
    val destCtor = dest
        .constructors
        .first { ctor ->
            ctor
                .parameters
                .filter { !it.isOptional }
                .all { param -> dest
                    .memberProperties
                    .any { matchProp(it, param) }
                }
        }
    // Look for matching properties with ctor parameters
    val argsWithProps: Map<KParameter, KProperty<*>?> = destCtor
        .parameters
        .associateWith { param -> src::class
            .memberProperties
            .firstOrNull() { matchProp(it, param) }
        }
        .filter { it.value != null }

    //args.forEach { t, u -> println("--> ($t | $u)") }
    val args: Map<KParameter, Any?> = argsWithProps
        .map { pair ->
            val propVal = pair.value?.call(src)
            pair.key to parseProp(pair.value!!.returnType, pair.key.type, propVal)
        }
        .associate { it }
    return destCtor.callBy(args)
}

fun matchProp(srcProp: KProperty<*>, param: KParameter) : Boolean {
    val fromName = srcProp.findAnnotation<MapProp>()?.paramName ?: srcProp.name
    if(fromName != param.name) {
        return false
    }
    val srcKlass = srcProp.returnType.classifier as KClass<*>
    return if(srcKlass.java.isPrimitive)
        srcProp.returnType == param.type
    else
        true
}

// Property parser considering:
// - primitive and String values
// - Objects non-primitives (recursively)
// - Lists (recursively)
fun parseProp(srcType: KType, destType: KType, propVal: Any?): Any? {
    if(propVal == null) return null

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
        // For instance, for List<Song>, Song is the type refered in the arguments list.
        // srcType.arguments[0].type?.classifier returns the class that represents the first type (e.g., Song).
        val elemSrcKlass = srcType.arguments[0].type?.classifier as KClass<*>
        val elemDestKlass = destType.arguments[0].type?.classifier as KClass<*>
        return (propVal as List<*>).map {
            it?.mapTo(elemDestKlass)
        }
    }
    // Object case:
    return propVal.mapTo(destKlass)
    // Other cases (e.g., Array, Maps, Enum) need to be implemented
}
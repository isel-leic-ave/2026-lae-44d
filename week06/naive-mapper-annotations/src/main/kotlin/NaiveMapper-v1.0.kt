package pt.isel

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * Version 1.0 of NaiveMapper (function) with createInstance
 * Restrictions:
 *  - constructor is parameterless;
 *  - only properties with the same name and type;
 *  - dest properties are mutable.
 */
fun <T : Any> Any.mapToProps(destClass: KClass<T>) : T {
    /**
     * 1st - for simplicity here, we consider the constructor is parameterless
     * in order to use the createInstance() function to create an instance.
     */
    val target = destClass.createInstance()

    /**
     * 2nd - now, we match source (this) and dest properties with the same
     * name (or annotation MapProp) and returnType and set the value of the
     * dest property with the value of the source property.
     */
    this::class.memberProperties.forEach { srcProp ->
        val destProp = destClass.memberProperties.firstOrNull {
            (it.name == (srcProp.findAnnotation<MapProp>()?.paramName ?: srcProp.name)
                    && it.returnType == srcProp.returnType)
        }
        // Check if dest property is mutable
        if(destProp != null && destProp is KMutableProperty<*>) {
            // Getting the source property value
            val srcValue = srcProp.call(this)
            // Setting the dest property value
            destProp.setter.call(target, srcValue)
        }
    }

    /**
     * 3rd - return the target object considering the restrictions
     */
    return target
}
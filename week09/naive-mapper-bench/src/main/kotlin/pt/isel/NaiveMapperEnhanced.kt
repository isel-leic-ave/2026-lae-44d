package pt.isel

import kotlin.collections.component1
import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * 4th version - NaiveMapper enhanced with a cache of parser functions of given a property value
 * and a NaiveMapper fabric.
 */
class NaiveMapperEnhanced<T : Any> private constructor(val srcType: KClass<*>, val destType:KClass<T>) {
    /**
     * The companion object implements the fabric:
     *     - mappers: static object to store the mapper instances
     *     - mapper: static function that get or put a mapper instance of/from a source type and a destiny type.
     *     Note: NaiveMapperFabric is a class with a private constructor, avoiding outside instantiation.
      */
    companion object {
        private val mappers = mutableMapOf< Pair<KClass<*>, KClass<*>>, NaiveMapperEnhanced<*> >()

        fun <T : Any> mapper(srcType: KClass<*>, destType:KClass<T>) : NaiveMapperEnhanced<T> {
            return mappers.getOrPut(srcType to destType) {
                NaiveMapperEnhanced(srcType, destType)
            } as NaiveMapperEnhanced<T>
        }
    }

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
        //.also { it.map { (param, prop) -> println("paramName: ${param.name}, propName: ${prop!!.name}") } }

    /**
     * Enhancement (for performance): creating a list of PropInfo.
     * PropInfo has the source property, the correspondent constructor
     * parameter and a function to make the parse of the value of
     * property source.
     */
    private val propInfoList: List<PropInfo> = args.map { (param, prop) ->
        // prop is KProperty<*>?, but we know it's not null due to args' filter
        val parseProp = buildParseProp(prop!!.returnType, param.type)
        PropInfo(prop, param, parseProp)
    }

    private fun matchPropWithParam(srcProp: KProperty<*>, param: KParameter) : Boolean {
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

    /**
     * Get the values of properties from source and pass them
     * to te constructor through the callBy() using the propInfoList
     */
    fun mapFrom(src: Any) : T {
        val arguments: Map<KParameter, Any?> = propInfoList.associate {
            val propValue = it.srcProp.call(src)
            it.destParam to it.mapPropValue(propValue)
        }
        return destCtor.callBy(arguments)
    }

    /**
     * Given a property and parameter type, returns a function to make
     * de parser of a property source value considering the cases:
     *     - Identity: types are equals
     *     - List case: property value is a list
     *     - Reference case: property value is reference type
     */
    private fun buildParseProp(propType: KType, paramType: KType): (Any?) -> Any? {
        // Trivial case: the value is the value
        /*if (propType == paramType) {
            return { propValue -> propValue }
        }*/

        val propKlass = propType.classifier as KClass<*>
        val paramKlass = paramType.classifier as KClass<*>

        // Primitive or String case
        if(propKlass.java.isPrimitive || paramKlass == String::class) {
            return { propValue -> propValue }
        }

        // List case:
        if (propKlass == List::class && paramKlass == List::class) {
            val propElemKlass = propType.arguments[0].type!!.classifier as KClass<*>
            val paramElemKlass = paramType.arguments[0].type!!.classifier as KClass<*>
            val mapper = mapper(propElemKlass, paramElemKlass)
            return { propValue ->
                (propValue as List<*>).map {
                    it?.let { mapper.mapFrom(it) }
                    //if(it == null) null else mapper.mapFrom(it)
                }
            }
        }
        // Reference case:
        val mapper = mapper(propKlass, paramKlass)
        return { propValue -> propValue?.let { mapper.mapFrom(it) } }
        //return { propValue -> if(propValue == null) null else mapper.mapFrom(propValue) }
        // TODO: Other cases (e.g., Array, Maps, Enum) need to be implemented
    }
}
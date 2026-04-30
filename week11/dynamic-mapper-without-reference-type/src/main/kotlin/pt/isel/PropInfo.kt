package pt.isel

import kotlin.reflect.KParameter
import kotlin.reflect.KProperty

data class PropInfo(
    val srcProp: KProperty<*>,
    val destParam: KParameter,
    val mapPropValue: (Any?) -> Any?,
)
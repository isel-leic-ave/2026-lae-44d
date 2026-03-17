package pt.isel

import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

// Property Logger using Kotlin API reflect
fun Appendable.log(obj: Any) {
    this.appendLine("Object of Type ${obj::class.simpleName}")
    obj::class.declaredMemberProperties.forEach { prop ->
        prop.isAccessible = true // e.g., if the property is private
        this.appendLine("  - ${prop.name}: ${prop.call(obj)}")
    }
}
package pt.isel

import java.lang.reflect.Method

// Property Logger using Java API reflect
fun Appendable.logJava(obj: Any) {
    val classJava: Class<*> = obj::class.java
    this.append("Object of Type " + classJava.getSimpleName() + System.lineSeparator())
    val mapProperty: MutableMap<String, Any> = mutableMapOf()
    // Fields
    classJava.declaredFields.forEach { field ->
        field.isAccessible = true
        val propName = field.name
        val propValue = field.get(obj)
        mapProperty += propName to propValue
    }
    // Property getters
    classJava.declaredMethods
        .filter { isGetterJava(it) }
        .forEach { mth ->
            mth.isAccessible = true
            val propName: String = mth.name.replaceFirst("get", "").replaceFirstChar { it.lowercase() }
            val propValue: Any = mth.invoke(obj)
            mapProperty += propName to propValue
        }
    mapProperty.keys.forEach { this.append("  - " + it + ": " + mapProperty[it] + System.lineSeparator()) }
}

fun Appendable.logGettersJava(obj: Any) {
    val classJava: Class<*> = obj::class.java
    this.appendLine("Object of Type ${classJava.getSimpleName()}" + System.lineSeparator())
    classJava.declaredMethods
        .filter { isGetterJava(it) }
        .forEach { mth ->
            mth.isAccessible = true
            val propName: String? = mth.name.replaceFirst("get", "").replaceFirstChar { it.lowercase() }
            val propValue: Any? = mth.invoke(obj)
            this.append("- " + propName + ": " + propValue + System.lineSeparator())
        }
}

fun isGetterJava(m: Method): Boolean {
    return m.name.startsWith("get")
            && m.parameters.size == 0
            && m.returnType != Void.TYPE
}

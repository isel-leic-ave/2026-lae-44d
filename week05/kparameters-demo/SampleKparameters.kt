import kotlin.reflect.KClass
import kotlin.reflect.full.*

fun main() {

    val lamp1 = Lamp() // Create an object Lamp
    println("lamp1 object member functions:")
    //val lampMemberFuncs = lamp1::class.declaredMemberFunctions
    val lampMemberFuncs = lamp1::class.memberFunctions
    lampMemberFuncs.forEach {
        println("\nFunction name: ${it.name}")
        println("Function return type: ${it.returnType} and return type classifier: ${it.returnType.classifier}")
        println("instanceParameter: ${it.instanceParameter}")
        println("parameters (size: ${it.parameters.size}):")
        it.parameters.forEach { println("   ${it} | ${it.name} | ${it.kind} | ${it.type}") }
    }
}
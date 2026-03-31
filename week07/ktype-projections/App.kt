import kotlin.reflect.KType
import kotlin.reflect.typeOf
import kotlin.reflect.full.declaredMemberProperties


fun main() {
    // Using typeOf function to get the Ktype of MutableMap<String, out Number?>
    val mapType: KType = typeOf< MutableMap<String, out Number?> >()
    val listType: KType = typeOf< List<Int> >()

    println("Classifier (Map): ${mapType.classifier}")

    mapType.arguments.forEachIndexed { index, projection ->
        val variance = projection.variance
        val projType = projection.type

        println("\n--- Argument [$index] ---")
        println("Variance: $variance")
        println("Projection type: $projType")
        println("Projection classifier: ${projType?.classifier}")
        println("Is Nullable? ${projType?.isMarkedNullable}")
    }

    println("\nClassifier (List): ${listType.classifier}")

    listType.arguments.forEachIndexed { index, projection ->
        val variance = projection.variance
        val projType = projection.type

        println("\n--- Argument [$index] ---")
        println("Variance: $variance")
        println("Projection type: $projType")
        println("Projection classifier: ${projType?.classifier}")
        println("Is Nullable? ${projType?.isMarkedNullable}")
    }

}
import kotlin.reflect.full.*

fun main() {
    println("Account class annotations:")
    Account::class
        .annotations
        .forEach { println(it) }

    println("\nDeclared properties annotations:")
    Account::class
        .declaredMemberProperties
        .forEach { prop ->
            if (prop.annotations.size == 0)
                println("  ${prop.name} has not annotation")
            prop.annotations.forEach { annot ->
                println("  ${prop.name} has $annot")
            }
        }

    println("\nConstructor parameter annotations:")
    Account::class
        .constructors
        .first()
        .parameters
        .forEach { par ->
            if (par.annotations.size == 0)
                println("  ${par.name} has not annotation")
            par.annotations.forEach { annot ->
                println("  ${par.name} has $annot")
            }
        }

    println("\nIs Account with Color? ${Account::class.hasAnnotation<Color>()}")
    println("Is Account with Tag? ${Account::class.hasAnnotation<Tag>()}")

    Account::class
        .declaredMemberFunctions
        .forEach {
            println("\nfunciton ${it.name} has annotations: ${it.annotations}")
            println("  parameter annotations:")
            it.parameters.forEach { param ->
                param.annotations.forEach { annot ->
                    println("   - ${param.name} has $annot")
                }
            }
        }

    println("\nDeclared properties annotations (getting labels):")
    Account::class
        .memberProperties
        .forEach {
            val color = it.findAnnotation<Color>()
            println("  ${it.name} prop has annotation Color with label ${color?.label}")
        }

    println("\nConstructor parameter annotations (getting labels):")
    Account::class
        .constructors
        .first()
        .parameters
        .forEach {
            val color = it.findAnnotation<Color>()
            if (color != null) println("  ${it.name} param has annotation Color with label ${color.label}")
        }
}
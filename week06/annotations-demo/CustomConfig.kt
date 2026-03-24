import java.util.concurrent.TimeUnit // An enum type example: {DAYS, HOURS, MINUTES, SECONDS, ...}
import kotlin.reflect.KClass

annotation class CustomConfig(
    val id: Int,                      // Primitivo
    val name: String = "Default",     // String com valor por omissão
    val target: KClass<*>,            // Classe
    val mode: TimeUnit,               // Enum
    val tags: Array<Tag>,             // Array
    val color: Color                   // Outra anotação
)
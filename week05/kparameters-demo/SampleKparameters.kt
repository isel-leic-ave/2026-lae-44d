import kotlin.reflect.KClass
import kotlin.reflect.typeOf
import kotlin.reflect.KCallable
import kotlin.reflect.full.*


class Notebook(val id: Int, val name: String) {
    // Initializing a MutableList to hold the string notes
    private val notes = mutableListOf<String>()

    // Function to add a note
    fun addNote(note: String) {
        notes.add(note)
        println("Note added: $note")
    }
}

fun main() {

    val myNotes = Notebook(123, "My notes") // Create an object myNotes

    println("myNotes object members:")
    //val myNotesMemberFuncs = myNotes::class.declaredMemberFunctions
    val myNotesMembers = myNotes::class.declaredMembers
    myNotesMembers.forEach {
        println("\nMember name: ${it.name}")
        // classifier property returns a KClassifier (KClass is subtype of KClassifier)
        val ktype = it.returnType
        val kclass = ktype.classifier as KClass<*>
        println("Member return type: ${ktype}")
        println("Member return classifier (kclass): ${kclass}")

        // Checking subtype: should use Ktype (typeOf<T>() is a inline function that returns the KType of T)
        println("${ktype} is subtype of Number ? " + ktype.isSubtypeOf(typeOf<Number>()))

        // Checking subclass: should use Kclass
        println("${kclass} is subclass of Number ? " + kclass.isSubclassOf(Number::class))

        // Parameter list:
        println("parameters (size: ${it.parameters.size}):")
        it.parameters.forEach { println("   ${it} --> ${it.name} | ${it.kind} | ${it.type}") } // name is null fot the instance parameter (this)
    }
}
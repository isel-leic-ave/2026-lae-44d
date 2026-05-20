enum class PauseState {
    VALUE_YIELDED,
    VALUE_NOT_YIELDED,
    FINISHED
}

// Similar to Continuation but only with resume part (which is a function representing the next operation)
typealias SimpleContinuation = () -> Unit

// Simple implementation of a sequence generator (for academic purpose) based on:
// https://github.com/JetBrains/kotlin/blob/2.3.20/libraries/stdlib/src/kotlin/collections/SequenceBuilder.kt
fun <T: Any> mySequence(block: SimpleSequenceIterator<T>.() -> Unit): Iterator<T>{
    return SimpleSequenceIterator(block)
}

class SimpleSequenceIterator<T : Any>(firstPart: SimpleSequenceIterator<T>.() -> Unit): Iterator<T> {
    private var state: PauseState = PauseState.VALUE_NOT_YIELDED
    private var value: T? = null
    private var nextPart: SimpleContinuation = { firstPart() } // Starts with the first block

    fun yield(value: T, nextPart: SimpleContinuation) {
        this.value = value
        this.nextPart = nextPart
        state = PauseState.VALUE_YIELDED
    }
    
    override fun hasNext(): Boolean {
        while(true) {
            when(this.state) {
                // The value is ready and can be consumed in a next call
                PauseState.VALUE_YIELDED -> return true
                // There is no value (return false)
                PauseState.FINISHED -> return false
                // nextPart will be resumed and can yield a value
                PauseState.VALUE_NOT_YIELDED -> {
                    this.state = PauseState.FINISHED
                    this.nextPart() // Resume
                }
            }            
        }
    }

    override fun next(): T {
        when(state) {
            PauseState.FINISHED -> throw NoSuchElementException()
            PauseState.VALUE_YIELDED -> {
                state = PauseState.VALUE_NOT_YIELDED
                return value!!
            }
            PauseState.VALUE_NOT_YIELDED -> {
                if(!hasNext()) throw NoSuchElementException()
                return value!!
            }
        }
    }
}

fun main() {
    val realSeq = sequence<Int> { // using the real sequence generator
        println("Yielding 1 and pausing...")
        yield(1)
        println("Resumed after 1, yielding 2 and pausing...")
        yield(2)
        println("Resumed after 2, yielding 3 and pausing...")
        yield(3)
        println("Resumed after 3, and finishing")
    }
    println("»»» Before consume the real sequence")
    realSeq.forEach {
        println("--> $it")
    }

    val seq = mySequence<Int> { // using my simple sequence generator
        println("Yielding 1 and pausing...")
        yield(1) {
            println("Resumed after 1, yielding 2 and pausing...")
            yield(2) {
                println("Resumed after 2, yielding 3 and pausing...")
                yield(3) {
                    println("Resumed after 3, and finishing")
                }
            }
        }
    }

    println("\n»»» Before consume the (my) sequence")
    seq.forEach {
        println("--> $it")
    }
}
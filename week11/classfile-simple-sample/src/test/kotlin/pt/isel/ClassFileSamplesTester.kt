package pt.isel

import org.junit.jupiter.api.Test
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.functions
import kotlin.test.assertEquals

class ClassFileSamplesTester {

    @Test
    fun `Testing GenBar class generated dynamically`() {

        BarGen.buildAndSave()

        val kClassBar = Unit::class.java.classLoader
            .loadClass("pt.isel.Bar")
            .kotlin

        val objBar = kClassBar.createInstance() // <=> val objBar = Bar()
        val foo = kClassBar.functions.first { it.name == "foo" }

        //println(foo.call(objBar))
        assertEquals(foo.call(objBar), 67895)
    }

    @Test
    fun `Testing GenCouter class generated dynamically`() {

        CounterGen.buildAndSave()

        val kClassCounter = Unit::class.java.classLoader
            .loadClass("pt.isel.Counter")
            .kotlin

        // Using interface Sum for casting the object
        val counter: Sum = kClassCounter
            .constructors.first()
            .call(7) as Sum // <=> val counter = Counter(7)

        assertEquals(counter.add(11), 18) // 7 + 11 = 18
    }
}
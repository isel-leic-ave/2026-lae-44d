package pt.isel

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

// Property Logger Tester
class LoggerTest {

    @Test fun checkLogKotlinReflectStudent() {
        val expected = """
            Object of Type Student
              - from: Portugal
              - name: Maria
              - nr: 9873479
        """.trimIndent()
        val st = Student("Maria", 9873479, "Portugal")
        val actual = StringBuilder().also {
            it.log(st)
        }
        //actual.log(st)
        println(actual)
        assertEquals(expected, actual.toString().trimIndent())
    }
    @Test fun checkLogJavaReflectStudent() {
        val expected = """
            Object of Type Student
              - name: Maria
              - nr: 9873479
              - from: Portugal
        """.trimIndent()
        val st = Student("Maria", 9873479, "Portugal")
        val actual = StringBuilder().also {
            it.logJava(st) // Kotlin code
            //Logger.log(it, st) // Java code
        }
        //actual.log(st)
        println(actual)
        assertEquals(expected, actual.toString().trimIndent())
    }
    @Test fun checkLogKotlinReflectRectangle() {
        val expected = """
            Object of Type Rectangle
              - area: 20
              - height: 5
              - width: 4
        """.trimIndent()
        val r = Rectangle(4, 5)
        val actual = StringBuilder().also {
            it.log(r)
        }
        println(actual)
        assertEquals(expected, actual.toString().trimIndent())
    }
    @Test fun checkLogJavaReflectRectangle() {
        val expected = """
            Object of Type Rectangle
              - width: 4
              - height: 5
              - area: 20
        """.trimIndent()
        val r = Rectangle(4, 5)
        val actual = StringBuilder().also {
            it.logJava(r)
        }
        println(actual)
        assertEquals(expected, actual.toString().trimIndent())
    }
}
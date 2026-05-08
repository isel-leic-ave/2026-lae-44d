package pt.isel

import org.junit.jupiter.api.Test
import pt.isel.weather.*
import java.time.LocalDate
import java.time.LocalDate.of
import java.time.Month
import kotlin.test.assertEquals

class TestTryAdvance {
    private val sidney2023List5Assert = listOf(13, 17, 18, 23, 25)

    @Test
    fun `iterate for 5 first weather description in tryAdvance with Spliterator`() {
        val pastWeather = loadWeathers("Sidney", 2023, Month.APRIL).take(5)
        val iter = pastWeather.stream().spliterator()
        while (iter.tryAdvance { w ->
                println("${w.date}: ${w.weatherDesc}")
            }){ /*while block here is empty*/ }
    }

    @Test
    fun `select top 5 temperatures in Sidney with imperative in tryAdvance mode with Spliterator`() {
        val pastWeather = loadWeathers("Sidney", 2023, Month.APRIL)
        val top5temps = mutableListOf<Int>()
        val iter = pastWeather.stream().spliterator()
        while (iter.tryAdvance { w ->
                if (w.isSunny) {               // <=> filter
                    top5temps.add(w.celsius)   // <=> map
                }
            }) {
            if (top5temps.size >= 5) { // <=> take
                break
            }
        }
        val actual = top5temps.iterator()
        sidney2023List5Assert
            .forEach { assertEquals(it, actual.next()) }
    }
}
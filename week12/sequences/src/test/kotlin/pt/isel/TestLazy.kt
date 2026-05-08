package pt.isel

import org.junit.jupiter.api.Test
import pt.isel.weather.*
import java.time.LocalDate.of
import java.time.Month
import kotlin.test.assertEquals

class TestLazy {
    private val sidney2023List5Assert = listOf(13, 17, 18, 23, 25)

    @Test
    fun `select top 5 temperatures in Sidney with pipeline eager`() {
        val pastWeather: List<Weather> = loadWeathers("Sidney", 2023, Month.APRIL)
        var count = 0
        val top5temps = pastWeather
            .filter { count++; it.isSunny }
            .map { count++; it.celsius }
            .take(5)
        val actual = top5temps.iterator()
        sidney2023List5Assert
            .forEach { assertEquals(it, actual.next()) }
        assertEquals(35, count)
    }

    @Test
    fun `select top 5 temperatures in Sidney with pipeline lazy`() {
        val pastWeather = loadWeathers("Sidney", 2023, Month.APRIL)
        var count = 0
        val top5temps = pastWeather
            .asSequence()
            .filter { count++; it.isSunny }
            .map { count++; it.celsius }
            .take(5)
        val actual = top5temps.iterator()
        sidney2023List5Assert
            .forEach { assertEquals(it, actual.next()) }
        assertEquals(18, count)
    }

    @Test
    fun `select top 5 temperatures in Sidney with pipeline lazy in Java (Stream)`() {
        val pastWeather = loadWeathers("Sidney", 2023, Month.APRIL)
        var count = 0
        val top5temps = pastWeather
            .stream()
            .filter { count++; it.isSunny }
            .map { count++; it.celsius }
            .limit(5)
        val actual = top5temps.iterator()
        sidney2023List5Assert
            .forEach { assertEquals(it, actual.next()) }
        assertEquals(18, count)
    }

}
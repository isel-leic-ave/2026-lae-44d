package pt.isel

import org.junit.jupiter.api.Test
import pt.isel.weather.Weather
import pt.isel.weather.fetchWeather
import pt.isel.weather.isSunny
import pt.isel.weather.loadWeathers
import java.time.Month
import kotlin.test.assertEquals

class TestWeatherWebApi {
    private val sidney2023List5Assert = listOf(13, 17, 18, 23, 25)

    @Test
    fun test(){
        fetchWeather("Lisbon", 2025, Month.MAY)
    }

    @Test
    fun `test load file with weather for Barcelona`() {
        loadWeathers("Barcelona", 2023, Month.JANUARY)
            .forEach { println(it) }
    }

    @Test
    fun `select top 5 temperatures in Sidney with pipeline`() {
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
    fun `select top 5 temperatures in Sidney with imperative`() {
        val pastWeather = loadWeathers("Sidney", 2023, Month.APRIL)
        val top5temps = mutableListOf<Int>()
        for (w in pastWeather) {
            if (w.isSunny) {               // <=> filter
                top5temps.add(w.celsius)   // <=> map
                if (top5temps.size >= 5) { // <=> take
                    break
                }
            }
        }
        val actual = top5temps.iterator()
        sidney2023List5Assert
            .forEach { assertEquals(it, actual.next()) }
    }

}
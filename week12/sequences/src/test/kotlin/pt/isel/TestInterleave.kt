package pt.isel

import org.junit.jupiter.api.Test
import pt.isel.weather.*
import java.time.LocalDate
import java.time.LocalDate.of
import java.time.Month
import kotlin.test.assertEquals

class TestInterleave {
    private val sidney2023List5Assert = listOf(13, 17, 18, 23, 25)

    @Test
    fun `check interleave explicit temperatures in celsius`() {
        val sidneyJan5days = loadWeathers("Sidney", 2024, Month.JULY).take(5)
        val sidneyFeb5days = loadWeathers("Sidney", 2024, Month.AUGUST).take(5)
        sidneyJan5days
            .asSequence()
            .map(Weather::celsius)
            .interleaveExt(sidneyFeb5days.asSequence().map(Weather::celsius))
            //.forEach { print("$it ") }
            .forEachIndexed { index, item ->
                if (index % 2 == 0) assertEquals(sidneyJan5days[index / 2].celsius, item)
                else assertEquals(sidneyFeb5days[index / 2].celsius, item)
            }
    }

    @Test
    fun `check interleave temperatures in celsius for Java streams`() {
        val sidneyJan5days = loadWeathers("Sidney", 2024, Month.JULY).take(5)//.toList().also{println(it.map(Weather::celsius))}
        val sidneyFeb5days = loadWeathers("Sidney", 2024, Month.AUGUST).take(5)//.toList().also{println(it.map(Weather::celsius))}
        var index = 0
        sidneyJan5days
            .stream()
            .map(Weather::celsius)
            .interleave(sidneyFeb5days.stream().map(Weather::celsius))
            .forEach { item -> // Note: there is no forEachIndex for a Stream
                //println(item)
                if (index % 2 == 0) assertEquals(sidneyJan5days[index / 2].celsius, item)
                else assertEquals(sidneyFeb5days[index / 2].celsius, item)
                index++
            }
    }

    @Test
    fun `check interleave generator temperatures in celsius`() {
        val sidneyJan5days = loadWeathers("Sidney", 2024, Month.JULY).take(5)
        val sidneyFeb5days = loadWeathers("Sidney", 2024, Month.AUGUST).take(5)
        sidneyJan5days
            .asSequence()
            .map(Weather::celsius)
            .interleaveGen(sidneyFeb5days.asSequence().map(Weather::celsius))
            .forEachIndexed { index, item ->
                println("$index: $item")
                if (index % 2 == 0) assertEquals(sidneyJan5days[index / 2].celsius, item)
                else assertEquals(sidneyFeb5days[index / 2].celsius, item)
            }
    }

}
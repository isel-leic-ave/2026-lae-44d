package pt.isel

import org.junit.jupiter.api.Test
import pt.isel.weather.*
import java.time.LocalDate
import java.time.LocalDate.of
import java.time.Month
import kotlin.test.assertEquals

class TestInterleave {

    val sidneyJan5days = loadWeathers("Sidney", 2024, Month.JULY).take(5).toList()//.also{println(it.map(Weather::celsius))}
    val sidneyFeb5days = loadWeathers("Sidney", 2024, Month.AUGUST).take(3).toList()//.also{println(it.map(Weather::celsius))}

    @Test
    fun `check interleave explicit temperatures in celsius`() {

        sidneyJan5days
            .asSequence()
            .map(Weather::celsius)
            .interleaveExt(sidneyFeb5days.asSequence().map(Weather::celsius))
            .forEachIndexed { index, item ->
                //println("$index: $item")
                if (index % 2 == 0) assertEquals(sidneyJan5days[index / 2].celsius, item)
                else assertEquals(sidneyFeb5days[index / 2].celsius, item)
            }
    }

    @Test
    fun `check interleave temperatures in celsius for Java streams`() {
        var index = 0
        sidneyJan5days
            .stream()
            .map(Weather::celsius)
            .interleave(sidneyFeb5days.stream().map(Weather::celsius))
            .forEach { item -> // Note: there is no forEachIndexed for a Stream
                //println("$index: $item")
                if (index % 2 == 0) assertEquals(sidneyJan5days[index / 2].celsius, item)
                else assertEquals(sidneyFeb5days[index / 2].celsius, item)
                index++
            }
    }

    @Test
    fun `check interleave temperatures in celsius using sequence and yield`() {
        sidneyJan5days
            .asSequence()
            .map(Weather::celsius)
            .interleave(sidneyFeb5days.asSequence().map(Weather::celsius))
            .forEachIndexed { index, item ->
                //println("$index: $item")
                if (index % 2 == 0) assertEquals(sidneyJan5days[index / 2].celsius, item)
                else assertEquals(sidneyFeb5days[index / 2].celsius, item)
            }
    }

    @Test
    fun `check interleave generator temperatures in celsius`() {
        sidneyJan5days
            .asSequence()
            .map(Weather::celsius)
            .interleaveGen(sidneyFeb5days.asSequence().map(Weather::celsius))
            .forEachIndexed { index, item ->
                //println("$index: $item")
                if (index % 2 == 0) assertEquals(sidneyJan5days[index / 2].celsius, item)
                else assertEquals(sidneyFeb5days[index / 2].celsius, item)
            }
    }

}
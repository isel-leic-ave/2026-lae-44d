package pt.isel

import org.junit.jupiter.api.Test
import pt.isel.weather.*
import java.time.LocalDate
import java.time.LocalDate.of
import java.time.Month
import kotlin.test.assertEquals

class TestGenerateSequence {
    private val sidney2023List5Assert = listOf(13, 17, 18, 23, 25)

    @Test
    fun `check rainy days in Lisbon`() {
        generateSequence(of(2024, 1, 1)) { // starting on 2024-1-1
            it.plusMonths(1) // infinity
        }
            .take(5)                   // 5 months
            .flatMap { loadWeathers("Lisbon", it.year, it.month) }  // In Lisbon
            .filter{ it.isRainy } // Rainy days
            .map(Weather::weatherDesc) // Select the description
            .distinct()                // unique values
            .forEach { println(it) }
    }

    @Test
    fun `test load files with weather for Sidney since 2023`() {
        generateSequence(of(2023, 1, 1)) { it.plusMonths(1) }
            .take(24) // 24 months
            .flatMap { loadWeathers("Sidney", it.year, it.month) }
            .forEach { println(it) }
    }

    @Test
    fun `check interleave temperatures in celsius with positions`() {
        val sidneyJan5days = loadWeathers("Sidney", 2023, Month.APRIL)
        sidneyJan5days
            .asSequence()
            .filter(Weather::isSunny)
            .map(Weather::celsius)
            .interleave(generateSequence(1) { it + 1 }.map { " ($it) " })
            .take(10)
            .forEach { print(it) }
    }

}
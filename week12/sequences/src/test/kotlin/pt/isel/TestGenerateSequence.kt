package pt.isel

import org.junit.jupiter.api.Test
import pt.isel.weather.*
import java.time.LocalDate
import java.time.LocalDate.of
import java.time.Month
import kotlin.test.assertEquals

class TestGenerateSequence {

    @Test
    fun `Check rainy days in Lisbon (4 months since 2026)`() {
        generateSequence(of(2026, 1, 1)) { // starting on 2024-1-1
            it.plusMonths(1) // infinity
        }
            .take(4)                   // 4 months
            .flatMap { loadWeathers("Lisbon", it.year, it.month) }  // In Lisbon
            .filter{ it.isRainy } // Rainy days
            .map(Weather::weatherDesc) // Select the description
            .distinct()                // unique values
            .forEach { println(it) }
    }

    @Test
    fun `Test to load Sydney's weather data for the 24 months since 2024`() {
        generateSequence(of(2024, 1, 1)) { it.plusMonths(1) } // infinity
            .take(24) // 24 months
            .flatMap { loadWeathers("Sidney", it.year, it.month) }
            .forEach { println(it) }
    }

    @Test
    fun `check interleave temperatures with positions (Sidney, April 2023)`() {
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
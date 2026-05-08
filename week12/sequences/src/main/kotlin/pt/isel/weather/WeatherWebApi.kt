package pt.isel.weather

import io.github.cdimascio.dotenv.dotenv
import java.io.File
import java.net.URI
import java.time.LocalDate
import java.time.Month
import java.util.logging.Logger
import kotlin.collections.filter
import kotlin.collections.filterIndexed


private val currentDir = System.getProperty("user.dir")
private const val HOST = "http://api.worldweatheronline.com/premium/v1/"
private const val PATH_PAST_WEATHER = "past-weather.ashx?q=%s&date=%s&enddate=%s&tp=24&format=csv&key=%s"
private const val PATH_SEARCH = "search.ashx?query=%s&format=csv&key=%s"
private val dotenv = dotenv()
private val WEATHER_KEY = System.getenv("API_WORLD_WEATHER_KEY")?: dotenv["API_WORLD_WEATHER_KEY"]
/**
 * past-weather API of World Weather Online is limited to 80 items per page, with 2 items per day.
 * For a month of 31 days, we need 62 items.
 * Thus, we fetch only 1 month from the given date.
 *
 * E.g. http://api.worldweatheronline.com/premium/v1/past-weather.ashx?q=lisbon&date=2024-01-01&enddate=2024-04-30&tp=24&format=csv&key=$WEATHER_KEY
 */
fun fetchWeather(location: String, year: Int, month: Month): String {
    val since = LocalDate.of(year, month, 1)
    val to = since.plusMonths(1).minusDays(1)

    Logger.getLogger("pt.isel").info("Fetching for $location in $since")

    val path = HOST + String.format(PATH_PAST_WEATHER, location, since, to, WEATHER_KEY)
    //URI(path).toURL().readText().lines().forEach { println("---> $it") }
    val pathToSaveLocally = "$currentDir/src/test/resources/past-weather-${location.lowercase()}-$since.csv"
    // Cache in file, if necessary
    return saveCsv(URI(path), pathToSaveLocally)
}

/**
 * Fetch from the web API or read locally from a file
 */
fun loadWeathers(location: String, year: Int, month: Month): List<Weather> {
    return fetchWeather(location,year, month)
        .lines()
        .parseLines(month)
        .map {
            it.fromCsvToWeather()
        }
}

fun List<String>.parseLines(month: Month): List<String> {
    return this
        .filter { !it.startsWith('#') } // Filter comments
        .drop(1)                        // Skip line: Not available
        .filterIndexed {                // Filter hourly info
                index, _ ->  index % 2 != 0
        }
        .filter { it.fromCsvToWeather().date.month <= month }
}

fun saveCsv(uri: URI, pathname: String): String {
    val file = File(pathname)
    return uri.toURL().readText().also {
        if (! file.exists()) {
            file.writeText(it)
            println("File was created: ${file.name}")
        } else {
            println("File already exists: ${file.name}")
        }
    }
}
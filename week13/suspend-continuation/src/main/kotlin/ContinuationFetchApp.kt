import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers.ofString
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED

// This is an implementation of Continuation interface
// that resumes with processing data title (result from the fetch).
object MyContinuation : Continuation<String> {
    override val context = EmptyCoroutineContext
    override fun resumeWith(result: Result<String>) {
        println("Resuming continuation...")
        processDataTitle(result.getOrThrow())
    }
}

private val httpClient: HttpClient = HttpClient.newHttpClient()
private val requestBuilder = HttpRequest.newBuilder()
private fun request(url: String) = requestBuilder.uri(URI.create(url)).build()

fun main() {
    /*
     * 1. Calling a declared suspend function (async fetch)
     */
    // A coroutine to run the suspend functions synchronously
    runBlocking {
        // Call the fetch suspend function
        val data = fetch("https://github.com/")
        processDataTitle(data)
    }

    /*
     * 2. Calling the suspend function (async fetch) using explicitly the Continuation
     */
    val fetchHandle = ::fetch as (String, Continuation<String>) -> Any?
    fetchHandle("https://kotlinlang.org/", MyContinuation)
    Thread.sleep(2000) // wait for the data

    /*
     * 3. Building a suspend function (sync fetch) and
     * calling it using explicitly the Continuation
     */
    fetchCpsSync("https://stackoverflow.com/", MyContinuation)

    /*
     * 4. Building a suspend function (async fetch) and
     * calling it using explicitly the Continuation
     */
    fetchCpsAsync("https://docs.oracle.com/en/java/", MyContinuation)
    println("Done fetchCpsAsync")
    Thread.sleep(2000) // wait for the data

    /*
     * 5. Reverting the fetchCpsSync (sync fetch) to a suspend function and
     * calling it in a runBlocking. It should work like 1.
     */
    val fetchSuspend = ::fetchCpsSync as (suspend (String) -> String)
    runBlocking {
        val data = fetchSuspend("https://github.com/")
        processDataTitle(data)
    }
}


// Async fetch using HTTPClient
suspend fun fetch(url: String): String {
    println("Fetching $url")
    return httpClient
        .sendAsync(request(url), ofString())
        .thenApply(HttpResponse<String>::body)
        .await()
}

// Sync fetch using readText in the Continuation Passing Style
fun fetchCpsSync(path: String, onComplete: Continuation<String>): Any {
    println("(CPS Sync) Fetching $path")
    try {
        val body = URI(path).toURL().readText() // sync fetch
        onComplete.resume(body)
    } catch (err: Throwable) {
        onComplete.resumeWithException(err)
    }
     // Here, the execution was suspended and will not return any result immediately.
     // !!! It makes sense with the asynchronous httpClient call !!!
    return COROUTINE_SUSPENDED
}

// Async fetch in the Continuation Passing Style
fun fetchCpsAsync(path: String, onComplete: Continuation<String>): Any {
    println("(CPS Async) Fetching $path")
    try {
        httpClient
            .sendAsync(request(path), ofString())
            .thenApply(HttpResponse<String>::body)
            .thenAccept { body -> onComplete.resume(body) }
    } catch (err: Throwable) {
        onComplete.resumeWithException(err)
    }
    // Here, the execution was suspended and will not return any result immediately.
    // !!! It makes sense with the asynchronous httpClient call !!!
    return COROUTINE_SUSPENDED
}

// A function to proccess the fetched data:
// prints the title of the content HTML page
fun processDataTitle(data: String){
    println(data.substringAfter("<title>").substringBefore("</title>"))
}
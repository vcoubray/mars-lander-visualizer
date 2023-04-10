package apis

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.browser.window

//val endpoint = window.location.origin
const val endpoint = "http://localhost:8080"

val jsonClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

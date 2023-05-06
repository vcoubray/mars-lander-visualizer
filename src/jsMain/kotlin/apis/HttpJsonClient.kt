package apis

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

//val endpoint = window.location.origin
const val endpoint = "http://localhost:8080"

val httpJsonClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

package plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureCORS() {
    install(CORS) {
//            allowMethod(HttpMethod.Get)
//            allowMethod(HttpMethod.Post)
//            allowMethod(HttpMethod.Delete)
        anyHost()
    }
}
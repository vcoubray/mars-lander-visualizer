package plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import routes.puzzleRouting
import routes.resourceRouting
import routes.simulationRouting

fun Application.configureRouting() {
    routing {
        resourceRouting()
        puzzleRouting()
        simulationRouting()
    }
}
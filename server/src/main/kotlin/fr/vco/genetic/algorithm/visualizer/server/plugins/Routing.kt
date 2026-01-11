package fr.vco.genetic.algorithm.visualizer.server.plugins

import fr.vco.genetic.algorithm.visualizer.server.routes.marslanding.marslandingRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*
import fr.vco.genetic.algorithm.visualizer.server.routes.resourceRouting

fun Application.configureRouting() {
    routing {
        resourceRouting()
        marslandingRouting()
    }
}
package fr.vco.genetic.algorithm.visualizer.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import fr.vco.genetic.algorithm.visualizer.routes.puzzleRouting
import fr.vco.genetic.algorithm.visualizer.routes.resourceRouting
import fr.vco.genetic.algorithm.visualizer.routes.simulationRouting

fun Application.configureRouting() {
    routing {
        resourceRouting()
        puzzleRouting()
        simulationRouting()
    }
}
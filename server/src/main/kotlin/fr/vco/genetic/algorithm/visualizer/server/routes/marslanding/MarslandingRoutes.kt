package fr.vco.genetic.algorithm.visualizer.server.routes.marslanding

import fr.vco.genetic.algorithm.visualizer.puzzle.PuzzleModule
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route

fun Routing.puzzleRouting(module: PuzzleModule<*, *>) {
    route(module.routePrefix) {
        simulationRouting(module)
        puzzleScenariosRouting()
        marsBenchmarkRouting()
    }
}

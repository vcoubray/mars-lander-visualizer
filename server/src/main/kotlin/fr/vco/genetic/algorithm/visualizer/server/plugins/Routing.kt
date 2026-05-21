package fr.vco.genetic.algorithm.visualizer.server.plugins

import fr.vco.genetic.algorithm.visualizer.puzzle.PuzzleModule
import fr.vco.genetic.algorithm.visualizer.puzzle.PuzzleRegistry
import fr.vco.genetic.algorithm.visualizer.server.routes.marslanding.puzzleRouting
import fr.vco.genetic.algorithm.visualizer.server.routes.resourceRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Application.configureRouting() {
    val registry = get<PuzzleRegistry>()
    routing {
        resourceRouting()
        for (module in registry.modules) {
            puzzleRouting(module)
        }
    }
}

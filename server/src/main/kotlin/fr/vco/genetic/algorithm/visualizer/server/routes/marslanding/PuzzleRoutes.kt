package fr.vco.genetic.algorithm.visualizer.server.routes.marslanding

import fr.vco.genetic.algorithm.visualizer.Puzzle
import fr.vco.genetic.algorithm.visualizer.server.routes.getIntParam
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import fr.vco.genetic.algorithm.visualizer.server.services.PuzzleService

fun Route.puzzleScenariosRouting() {

    val puzzleService by inject<PuzzleService>()

    route(Puzzle.path) {
        get {
            call.respond(puzzleService.puzzles)
        }

        get("/{puzzleId}") {
            val id = call.getIntParam("puzzleId")
            val puzzle = puzzleService.getPuzzle(id)
                ?: throw NotFoundException("No Puzzle found with id [$id]")
            call.respond(puzzle)

        }
    }
}
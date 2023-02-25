package routes

import PUZZLES
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import services.PuzzleService

fun Route.puzzleRouting() {

    val puzzleService by inject<PuzzleService>()

    route("/puzzles") {
        get {
            call.respond(puzzleService.puzzles)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.response.status(HttpStatusCode.BadRequest)
                return@get
            }

            val puzzle = PUZZLES.getOrNull(id)
            if (puzzle == null) {
                call.response.status(HttpStatusCode.NotFound)
                call.respondText("No puzzle with id [$id]")
                return@get
            }

            call.respond(puzzle)

        }
    }
}
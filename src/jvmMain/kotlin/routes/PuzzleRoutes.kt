package routes

import PUZZLES
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.puzzleRouting() {
    route("/puzzles") {
        get {
            call.respond(PUZZLES)
        }
    }
}
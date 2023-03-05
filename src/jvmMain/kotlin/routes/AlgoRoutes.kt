package routes

import AlgoSettings
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import services.AlgoService

val algoService = AlgoService()

fun Route.algoRouting() {
    route("/algo") {

        post("/reset") {
            val settings = call.receive<AlgoSettings>()
            call.respond(algoService.reset(settings))
        }

        get("/next") {
            call.respond(algoService.next())
        }

        post("/play") {
            val settings = call.receive<AlgoSettings>()
            call.respond(algoService.play(settings))
        }

        get("/test"){
            call.respondText ("TEST OK ")
        }
    }
}
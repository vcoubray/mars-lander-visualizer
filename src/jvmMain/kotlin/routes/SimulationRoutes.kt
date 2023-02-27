package routes

import AlgoSettings
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

import services.SimulationService

fun Route.simulationRouting() {

    val simulationService by inject<SimulationService>()

    route("/simulations") {
        get{
            call.respond(simulationService.getSimulationSummaries())
        }

        post{
            val settings = call.receive<AlgoSettings>()
            val id = simulationService.start(settings)
            call.respondText("$id")
        }

        route("/{id}") {
            get {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val result = simulationService.getSimulationSummary(id)
                if (result == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(result)
            }
            route("/generations") {


                get{
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@get
                    }
                    val result = simulationService.getGenerationSummaries(id)
                    if (result == null) {
                        call.respond(HttpStatusCode.NotFound)
                        return@get
                    }
                    call.respond(result)
                }

                get("/{generationId}") {
                    val id = call.parameters["id"]?.toIntOrNull()
                    val generationId = call.parameters["generationId"]?.toIntOrNull()
                    if (id == null || generationId == null) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@get
                    }
                    val result = simulationService.getGenerationSummary(id, generationId)
                    if (result == null) {
                        call.respond(HttpStatusCode.NotFound)
                        return@get
                    }
                    call.respond(result)
                }
            }
        }


    }
}
package routes

import AlgoSettings
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import services.SimulationService

fun Route.simulationRouting() {

    val simulationService by inject<SimulationService>()

    route("/simulations") {
        get {
            call.respond(simulationService.getSimulationSummaries())
        }

        post {
            val settings = call.receive<AlgoSettings>()
            val id = simulationService.start(settings)
            call.respondText("$id")
        }

        route("/{simulationId}") {
            get {
                val simulationId = call.getIntParam("simulationId")
                val result = simulationService.getSimulationSummary(simulationId)
                    ?: throw NotFoundException("No simulation found with id [$simulationId]")
                call.respond(result)
            }
            route("/generations") {
                get {
                    val simulationId = call.getIntParam("simulationId")
                    val result = simulationService.getGenerationSummaries(simulationId)
                        ?: throw NotFoundException("No simulation found with id [$simulationId]")
                    call.respond(result)
                }

                get("/{generationId}") {
                    val simulationId = call.getIntParam("simulationId")
                    val generationId = call.getIntParam("generationId")

                    val result = simulationService.getGeneration(simulationId, generationId)
                        ?: throw NotFoundException("No Generation found for simulation [$simulationId] and generation [$generationId]")
                    call.respond(result)
                }
            }
        }
    }
}

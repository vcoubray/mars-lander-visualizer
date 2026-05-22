package fr.vco.genetic.algorithm.visualizer.server.routes.admin

import fr.vco.genetic.algorithm.visualizer.persistence.BenchmarkRepository
import fr.vco.genetic.algorithm.visualizer.persistence.SimulationRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.adminRouting() {
    val simulationRepo by inject<SimulationRepository>()
    val benchmarkRepo by inject<BenchmarkRepository>()

    route("/admin") {
        post("/reset") {
            simulationRepo.deleteAll()
            benchmarkRepo.deleteAll()
            call.respond(status = HttpStatusCode.NoContent, message = "")
        }
    }
}

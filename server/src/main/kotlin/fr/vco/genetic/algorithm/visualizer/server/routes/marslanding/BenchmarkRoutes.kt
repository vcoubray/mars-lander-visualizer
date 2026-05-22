package fr.vco.genetic.algorithm.visualizer.server.routes.marslanding

import fr.vco.genetic.algorithm.visualizer.BenchmarkSettings
import fr.vco.genetic.algorithm.visualizer.MarsEngineSettings
import fr.vco.genetic.algorithm.visualizer.server.routes.getIntParam
import fr.vco.genetic.algorithm.visualizer.server.services.BenchmarkService
import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.benchmarkRouting() {
    val benchmarkService by inject<BenchmarkService>()

    route("/benchmarks") {
        get {
            call.respond(benchmarkService.getAll())
        }

        post {
            val settings = call.receive<BenchmarkSettings<MarsEngineSettings>>()
            val id = benchmarkService.start(settings)
            call.respondText("$id")
        }

        route("/{benchmarkId}") {
            get {
                val benchmarkId = call.getIntParam("benchmarkId")
                val result = benchmarkService.get(benchmarkId)
                    ?: throw NotFoundException("No benchmark found with id [$benchmarkId]")
                call.respond(result)
            }

            delete {
                val benchmarkId = call.getIntParam("benchmarkId")
                if (!benchmarkService.delete(benchmarkId))
                    throw NotFoundException("No benchmark found with id [$benchmarkId]")
                call.respond(status = HttpStatusCode.NoContent, message = "")
            }
        }
    }
}

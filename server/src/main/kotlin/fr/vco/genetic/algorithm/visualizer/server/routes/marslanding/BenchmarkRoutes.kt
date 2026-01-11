package fr.vco.genetic.algorithm.visualizer.server.routes.marslanding

import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import fr.vco.genetic.algorithm.visualizer.server.services.BenchmarkService


fun Route.marsBenchmarkRouting(){

    val benchmarkService by inject<BenchmarkService>()


    route("/benchmark") {
        get {
            call.respond(benchmarkService.getResults())
        }
    }

}
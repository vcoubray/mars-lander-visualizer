package fr.vco.genetic.algorithm.visualizer.routes

import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import fr.vco.genetic.algorithm.visualizer.services.BenchmarkService


fun Route.BenchmarkRouting(){

    val benchmarkService by inject<BenchmarkService>()


    route("/benchmark") {
        get {
            call.respond(benchmarkService.getResults())
        }
    }

}
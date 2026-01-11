package fr.vco.genetic.algorithm.visualizer.server.routes.marslanding

import io.ktor.server.routing.Routing
import io.ktor.server.routing.route

fun Routing.marslandingRouting (){

    route("/mars-landing"){
        marsSimulationRouting()
        marsPuzzleRouting()
        marsBenchmarkRouting()
    }
}
package routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import services.BenchmarkService


fun Route.BenchmarkRouting(){

    val benchmarkService by inject<BenchmarkService>()


    route("/benchmark") {
        get {
            call.respond(benchmarkService.getResults())
        }
    }

}
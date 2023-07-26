package plugins

import exceptions.AlreadyRunningException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is BadRequestException -> call.respondText(cause.message ?: "", status = HttpStatusCode.BadRequest)
                is NotFoundException -> call.respondText(cause.message ?: "", status = HttpStatusCode.NotFound)
                is AlreadyRunningException -> call.respondText(cause.message?: "A simulation is already running", status = HttpStatusCode.Conflict)
                else -> call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}
package routes

import io.ktor.server.application.*
import io.ktor.server.plugins.*


fun ApplicationCall.getParam(name: String): String =
    parameters[name] ?: throw BadRequestException("[$name] is mandatory")

fun ApplicationCall.getIntParam(name: String) =
    getParam(name).toIntOrNull() ?: throw BadRequestException("[$name] should be an Integer")
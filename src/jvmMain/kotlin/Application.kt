import io.ktor.server.application.*
import io.ktor.server.netty.*
import plugins.configureCORS
import plugins.configureCompression
import plugins.configureContentNegotiation
import plugins.configureRouting

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    configureContentNegotiation()
    configureCORS()
    configureCompression()
    configureRouting()
}

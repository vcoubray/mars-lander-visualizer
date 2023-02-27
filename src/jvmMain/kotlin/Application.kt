import io.ktor.server.application.*
import io.ktor.server.netty.*
import plugins.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    configureKoin()
    configureContentNegotiation()
    configureCORS()
    configureCompression()
    configureStatusPages()
    configureRouting()
}

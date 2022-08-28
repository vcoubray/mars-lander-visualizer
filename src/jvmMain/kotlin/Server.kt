import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import services.AlgoService

val algoService = AlgoService()

fun main() {

    embeddedServer(Netty, 8080) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
//            allowMethod(HttpMethod.Get)
//            allowMethod(HttpMethod.Post)
//            allowMethod(HttpMethod.Delete)
            anyHost()
        }
        install(Compression) {
            gzip()
        }

        routing {
            get("/") {
                call.respondText(
                    this::class.java.classLoader.getResource("index.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
            static("/") {
                resources("")
            }

            get("/puzzles") {
                call.respond(PUZZLES)
            }

            route("/algo"){
                post("/reset") {
                    val settings = call.receive<AlgoSettings>()
                    call.respond(algoService.reset(settings))
                }

                get("/next") {
                    call.respond(algoService.next())
                }

                post ("/play") {
                    val settings = call.receive<AlgoSettings>()
                    call.respond(algoService.play(settings))
                }
            }
        }
    }.start(wait = true)
}
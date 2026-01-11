package fr.vco.genetic.algorithm.visualizer

import fr.vco.genetic.algorithm.visualizer.server.plugins.configureCORS
import fr.vco.genetic.algorithm.visualizer.server.plugins.configureCompression
import fr.vco.genetic.algorithm.visualizer.server.plugins.configureContentNegotiation
import fr.vco.genetic.algorithm.visualizer.server.plugins.configureKoin
import fr.vco.genetic.algorithm.visualizer.server.plugins.configureRouting
import fr.vco.genetic.algorithm.visualizer.server.plugins.configureStatusPages
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    configureKoin()
    configureContentNegotiation()
    configureCORS()
    configureCompression()
    configureStatusPages()
    configureRouting()
}

package fr.vco.genetic.algorithm.visualizer.server.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.compression.*

fun Application.configureCompression() {
    install(Compression) {
        gzip()
    }
}
package fr.vco.genetic.algorithm.visualizer.server.plugins

import io.ktor.server.application.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import fr.vco.genetic.algorithm.visualizer.server.services.AlgorithmFactory

import fr.vco.genetic.algorithm.visualizer.server.services.PuzzleService
import fr.vco.genetic.algorithm.visualizer.server.services.SimulationService
import fr.vco.genetic.algorithm.visualizer.server.services.ServerStatusService

fun Application.configureKoin() {

    install(Koin) {
        slf4jLogger()

        modules (
            module {
                singleOf(::ServerStatusService)
                singleOf(::PuzzleService)
                singleOf(::SimulationService)
                singleOf(::AlgorithmFactory)
            }
        )
    }

}
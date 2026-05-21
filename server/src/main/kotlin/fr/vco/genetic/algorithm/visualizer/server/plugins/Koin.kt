package fr.vco.genetic.algorithm.visualizer.server.plugins

import fr.vco.genetic.algorithm.visualizer.marslanding.MarsPuzzleModule
import fr.vco.genetic.algorithm.visualizer.puzzle.PuzzleModule
import fr.vco.genetic.algorithm.visualizer.puzzle.PuzzleRegistry
import fr.vco.genetic.algorithm.visualizer.server.services.PuzzleService
import fr.vco.genetic.algorithm.visualizer.server.services.ServerStatusService
import fr.vco.genetic.algorithm.visualizer.server.services.SimulationService
import io.ktor.server.application.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(
            module {
                singleOf(::ServerStatusService)
                single { MarsPuzzleModule() } bind PuzzleModule::class
                single { PuzzleRegistry(getAll()) }
                singleOf(::PuzzleService)
                singleOf(::SimulationService)
            }
        )
    }
}

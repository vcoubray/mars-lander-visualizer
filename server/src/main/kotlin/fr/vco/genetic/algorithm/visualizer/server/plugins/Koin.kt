package fr.vco.genetic.algorithm.visualizer.server.plugins

import fr.vco.genetic.algorithm.visualizer.marslanding.MarsPuzzleModule
import fr.vco.genetic.algorithm.visualizer.persistence.BenchmarkRepository
import fr.vco.genetic.algorithm.visualizer.persistence.SimulationRepository
import fr.vco.genetic.algorithm.visualizer.persistence.buildDatabase
import fr.vco.genetic.algorithm.visualizer.puzzle.PuzzleModule
import fr.vco.genetic.algorithm.visualizer.puzzle.PuzzleRegistry
import fr.vco.genetic.algorithm.visualizer.server.services.BenchmarkService
import fr.vco.genetic.algorithm.visualizer.server.services.PuzzleService
import fr.vco.genetic.algorithm.visualizer.server.services.ServerStatusService
import fr.vco.genetic.algorithm.visualizer.server.services.SimulationService
import io.ktor.server.application.*
import kotlinx.serialization.json.Json
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
                single { buildDatabase() }
                single { Json { ignoreUnknownKeys = true } }
                singleOf(::ServerStatusService)
                single { MarsPuzzleModule() } bind PuzzleModule::class
                single { PuzzleRegistry(getAll()) }
                singleOf(::PuzzleService)
                singleOf(::SimulationRepository)
                singleOf(::BenchmarkRepository)
                singleOf(::SimulationService)
                singleOf(::BenchmarkService)
            }
        )
    }
}

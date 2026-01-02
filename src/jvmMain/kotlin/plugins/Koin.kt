package plugins

import io.ktor.server.application.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import services.AlgorithmFactory

import services.PuzzleService
import services.SimulationService
import services.ServerStatusService

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
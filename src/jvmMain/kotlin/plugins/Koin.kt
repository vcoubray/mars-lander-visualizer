package plugins

import io.ktor.server.application.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

import services.PuzzleService
import services.SimulationService

fun Application.configureKoin() {

    install(Koin) {
        slf4jLogger()

        modules (
            module {
                singleOf(::PuzzleService)
                singleOf(::SimulationService)
            }
        )
    }

}
package fr.vco.genetic.algorithm.visualizer.puzzle

import fr.vco.genetic.algorithm.visualizer.EngineSettings
import fr.vco.genetic.algorithm.visualizer.GenerationResult
import fr.vco.genetic.algorithm.visualizer.LimitType
import fr.vco.genetic.algorithm.visualizer.MarsChromosomeResult
import fr.vco.genetic.algorithm.visualizer.Puzzle
import fr.vco.genetic.algorithm.visualizer.SimulationSettings
import fr.vco.genetic.algorithm.visualizer.core.Chromosome
import fr.vco.genetic.algorithm.visualizer.core.GeneticAlgorithm
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import kotlin.reflect.KClass

interface PuzzleModule<S : EngineSettings, C : Chromosome> {
    val id: String
    val routePrefix: String
    val settingsClass: KClass<S>
    val scenarios: List<Puzzle>

    fun buildAlgorithm(settings: SimulationSettings<S>): GeneticAlgorithm<C>
    fun mapChromosome(chromosome: C, id: Int): MarsChromosomeResult
    suspend fun receiveSettings(call: ApplicationCall): SimulationSettings<S>

    @Suppress("UNCHECKED_CAST")
    fun runSimulation(settings: SimulationSettings<*>, onGeneration: (GenerationResult) -> Unit) {
        val typed = settings as SimulationSettings<S>
        val algo = buildAlgorithm(typed)
        val limitValue = settings.globalSettings.limitValue
        val runFn = when (settings.globalSettings.limitType) {
            LimitType.TIME -> algo::runUntilTime
            LimitType.SCORE -> algo::runUntilScore
        }
        runFn(limitValue) { population ->
            onGeneration(GenerationResult(population.mapIndexed { i, c -> mapChromosome(c, i) }))
        }
    }

    suspend fun receiveSettingsAny(call: ApplicationCall): SimulationSettings<*> = receiveSettings(call)
}

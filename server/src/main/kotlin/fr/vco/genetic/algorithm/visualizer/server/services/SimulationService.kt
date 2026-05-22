package fr.vco.genetic.algorithm.visualizer.server.services

import fr.vco.genetic.algorithm.visualizer.SimulationSettings
import fr.vco.genetic.algorithm.visualizer.puzzle.PuzzleRegistry
import fr.vco.genetic.algorithm.visualizer.persistence.SimulationRepository
import fr.vco.genetic.algorithm.visualizer.server.exceptions.AlreadyRunningException
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class SimulationService(
    private val registry: PuzzleRegistry,
    private val statusService: ServerStatusService,
    private val repo: SimulationRepository,
) {
    fun start(simulationSettings: SimulationSettings<*>): Int {
        if (!statusService.isRunning.compareAndSet(false, true)) throw AlreadyRunningException()
        val id = repo.insertPending(simulationSettings)
        val module = registry.findBySettings(simulationSettings)

        thread {
            try {
                var generationIdx = 0
                var lastBest = 0.0
                val duration = measureTimeMillis {
                    module.runSimulation(simulationSettings) { generation ->
                        val generationId = repo.insertGeneration(id, generationIdx++, generation)
                        repo.insertChromosomes(generationId, generation.population)
                        lastBest = generation.best
                    }
                }
                repo.complete(id, duration, lastBest, generationIdx)
                println("Simulation '$id' complete")
            } finally {
                statusService.isRunning.set(false)
            }
        }
        return id
    }

    fun getSimulationSummary(id: Int) = repo.findSummary(id)
    fun getSimulationSummaries() = repo.findSummaries()
    fun getGenerationSummaries(simulationId: Int) = repo.findGenerationSummaries(simulationId)
    fun getGeneration(simulationId: Int, generationId: Int) = repo.findGeneration(simulationId, generationId)
    fun deleteSimulation(id: Int): Boolean = repo.delete(id)
}

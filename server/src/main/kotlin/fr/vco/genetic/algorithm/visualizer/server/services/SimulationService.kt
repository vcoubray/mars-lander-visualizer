package fr.vco.genetic.algorithm.visualizer.server.services

import fr.vco.genetic.algorithm.visualizer.GenerationResult
import fr.vco.genetic.algorithm.visualizer.SimulationResult
import fr.vco.genetic.algorithm.visualizer.SimulationSettings
import fr.vco.genetic.algorithm.visualizer.SimulationStatus
import fr.vco.genetic.algorithm.visualizer.puzzle.PuzzleRegistry
import fr.vco.genetic.algorithm.visualizer.server.exceptions.AlreadyRunningException
import fr.vco.genetic.algorithm.visualizer.toSummary
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class SimulationService(
    private val registry: PuzzleRegistry,
    private val statusService: ServerStatusService,
) {
    private val simulations: MutableMap<Int, SimulationResult> = mutableMapOf()
    private var lastId = 0

    fun start(simulationSettings: SimulationSettings<*>): Int {
        if (!statusService.isRunning.compareAndSet(false, true)) throw AlreadyRunningException()
        val id = lastId++
        simulations[id] = SimulationResult(id, simulationSettings)
        val module = registry.findBySettings(simulationSettings)

        thread {
            val generations = mutableListOf<GenerationResult>()
            val duration = measureTimeMillis {
                module.runSimulation(simulationSettings) { generation ->
                    generations.add(generation)
                }
            }
            simulations[id]?.apply {
                bestScore = generations.last().best
                this.duration = duration
                this.generations = generations
                status = SimulationStatus.COMPLETE
            }
            println("Simulation '$id' complete")
            statusService.isRunning.set(false)
        }
        return id
    }

    fun getSimulationSummary(id: Int) = simulations[id]?.toSummary()
    fun getSimulationSummaries() = simulations.values.map { it.toSummary() }
    fun getGenerationSummaries(simulationId: Int) = simulations[simulationId]?.generations?.map { it.toSummary() }
    fun getGeneration(simulationId: Int, generationId: Int) = simulations[simulationId]?.generations?.getOrNull(generationId)
    fun deleteSimulation(id: Int) = simulations.remove(id)
}

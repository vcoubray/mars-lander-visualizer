package services

import AlgoSettings
import GenerationResult
import SimulationResult
import SimulationStatus
import condigame.*
import toSummary
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class SimulationService(
    private val algorithmFactory: AlgorithmFactory
) {
    private val simulations: MutableMap<Int, SimulationResult> = mutableMapOf()
    private var lastId = 0

    fun start(settings: AlgoSettings): Int {
        val id = lastId++
        simulations[id] = SimulationResult(id, settings)

        thread {
            val algo = algorithmFactory.fromSettings(settings)
            val generations = mutableListOf<GenerationResult>()
            val duration = measureTimeMillis {
                algo.runUntilTime(1000) { generation ->
                    generations.add(
                        GenerationResult(
                            generation.mapIndexed { i, it -> it.toResult(i) }
                        )
                    )
                }
            }
            simulations[id]?.apply {
                this.bestScore = generations.last().best
                this.duration = duration
                this.generations = generations
                this.status = SimulationStatus.COMPLETE
            }
            println("Simulation '$id' complete")
        }
        return id
    }

    fun getSimulationSummary(id: Int) = simulations[id]?.toSummary()

    fun getSimulationSummaries() = simulations.values.map { it.toSummary() }

    fun getGenerationSummaries(simulationId: Int) =
        simulations[simulationId]?.generations?.map { it.toSummary() }

    fun getGeneration(simulationId: Int, generationId: Int) =
        simulations[simulationId]?.generations?.getOrNull(generationId)

    fun deleteSimulation(id: Int) = simulations.remove(id)
}

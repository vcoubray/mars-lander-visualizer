package services

import GenerationResult
import SimulationResult
import SimulationSettings
import SimulationStatus
import condigame.*
import exceptions.AlreadyRunningException
import toSummary
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class SimulationService(
    private val algorithmFactory: AlgorithmFactory
) {
    private val simulations: MutableMap<Int, SimulationResult> = mutableMapOf()
    private var lastId = 0

    private val isRunning = AtomicBoolean(false)

    fun start(simulationSettings : SimulationSettings<*>): Int {
        if (!isRunning.compareAndSet(false,true)) {
            throw AlreadyRunningException()
        }
        val id = lastId++
        simulations[id] = SimulationResult(id, simulationSettings)

        thread {
            val algo = algorithmFactory.fromSettings(simulationSettings)
            val generations = mutableListOf<GenerationResult>()
            val runFunction = when (simulationSettings.limitType) {
                LimitType.TIME -> algo::runUntilTime
                LimitType.SCORE -> algo::runUntilScore
            }
            val duration = measureTimeMillis {
                runFunction(simulationSettings.limitValue) { generation ->
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
            isRunning.set(false)
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

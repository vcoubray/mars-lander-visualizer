package services

import AlgoSettings
import Generation
import GenerationSummary
import SimulationResult
import SimulationSummary
import algorithm.*
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis


class SimulationService(
    private val puzzleService: PuzzleService,
) {


    val simulations: MutableList<SimulationResult> = mutableListOf()


    fun start(settings: AlgoSettings): Int {
        val id = simulations.size

        simulations.add(SimulationResult(id))

        thread {
            val algo = settings.toAlgo()
            val generations: List<Generation>
            val duration = measureTimeMillis {
                generations = algo.runUntilTime(5000)
            }
            simulations[id].apply {
                this.bestScore = generations.last().best
                this.duration = duration
                this.generations = generations
                this.status = SimulationStatus.COMPLETE
            }
            println("Simulation complete")
        }

        return id
    }

    fun getSimulationSummary(id: Int) = simulations.getOrNull(id)?.toSummary()

    fun getSimulationSummaries() = simulations.map { it.toSummary() }

    fun getGenerationSummaries(simulationId: Int) =
        simulations.getOrNull(simulationId)?.generations?.map { it.toSummary() }

    fun getGenerationSummary(simulationId: Int, generationId: Int) =
        simulations.getOrNull(simulationId)?.generations?.getOrNull(generationId)


    private fun AlgoSettings.toAlgo() = GeneticAlgorithmImpl(
        puzzleService.getPuzzle(puzzleId)!!.toSurface(),
        puzzleService.getPuzzle(puzzleId)!!.initialState,
        chromosomeSize,
        populationSize,
        mutationProbability,
        elitismPercent,
        speedMax,
        xSpeedWeight,
        ySpeedWeight,
        rotateWeight,
        distanceWeight
    )

    private fun SimulationResult.toSummary() = SimulationSummary(
        id = this.id,
        status = this.status,
        duration = this.duration,
        bestScore = this.bestScore,
        generationCount = this.generations.size
    )

    private fun Generation.toSummary() = GenerationSummary(
        populationSize = this.population.size,
        best = this.best,
        mean = this.mean
    )

}
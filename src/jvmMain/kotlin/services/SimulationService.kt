package services

import AlgoSettings
import Generation
import GenerationSummary
import Puzzle
import SimulationResult
import SimulationSummary
import algorithm.*
import codingame.HEIGHT
import codingame.WIDTH
import condigame.Point
import condigame.Segment
import condigame.Surface
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis


class SimulationService(
    private val puzzleService: PuzzleService,
) {


    val simulations: MutableMap<Int,SimulationResult> = mutableMapOf()
    var lastId = 0

    fun start(settings: AlgoSettings): Int {
        val id = lastId++

        simulations[id] = SimulationResult(id, settings)

        thread {
            val algo = settings.toAlgo()
            val generations: List<Generation>
            val duration = measureTimeMillis {
                generations = algo.runUntilTime(1000)
            }
            simulations[id]?.apply {
                this.bestScore = generations.last().best
                this.duration = duration
                this.generations = generations
                this.status = SimulationStatus.COMPLETE
            }
            println("Simulation complete")
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
        settings = this.settings,
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
fun Puzzle.toSurface() = Surface(
    HEIGHT, WIDTH,
    surface.split(" ")
        .asSequence()
        .map { it.toDouble() }
        .chunked(2)
        .map { (x, y) -> Point(x, y) }
        .windowed(2)
        .map { (a, b) -> Segment(a, b) }
        .toList()
)

package services

import AlgoSettings
import GenerationResult
import Puzzle
import SimulationResult
import SimulationStatus
import algorithm.GeneticAlgorithmImpl
import codingame.HEIGHT
import codingame.WIDTH
import condigame.*
import toSummary
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis


class SimulationService(
    private val puzzleService: PuzzleService,
) {

    private val simulations: MutableMap<Int, SimulationResult> = mutableMapOf()
    private var lastId = 0

    fun start(settings: AlgoSettings): Int {
        val id = lastId++

        simulations[id] = SimulationResult(id, settings)

        thread {
            val algo = settings.toNewAlgo()
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

    private fun AlgoSettings.toNewAlgo() = GeneticAlgorithmImpl(
        MarsEngine(
            puzzleService.getPuzzle(puzzleId)!!.toSurface(),
            puzzleService.getPuzzle(puzzleId)!!.initialState,
            speedMax,
            xSpeedWeight,
            ySpeedWeight,
            rotateWeight,
            distanceWeight
        ),
        chromosomeSize,
        populationSize,
        mutationProbability,
        elitismPercent
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

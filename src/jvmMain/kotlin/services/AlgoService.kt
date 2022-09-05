package services

import AlgoSettings
import Config
import GenerationResult
import PUZZLES
import Puzzle
import RunStats
import codingame.HEIGHT
import codingame.WIDTH
import condigame.GeneticAlgorithm
import condigame.Point
import condigame.Segment
import condigame.Surface
import kotlin.system.measureTimeMillis

class AlgoService {
    var settings: AlgoSettings = Config.defaultSettings
    var algo = settings.toAlgo()
    var generationCount = 0


    @Synchronized
    fun reset(settings: AlgoSettings? = null): GenerationResult {
        settings?.let { this.settings = it }
        generationCount = 0
        algo = this.settings.toAlgo()
        return GenerationResult(emptyList(), this.generationCount)
    }

    @Synchronized
    fun next(): GenerationResult {
        generationCount++
        algo.next()
        return GenerationResult(this.algo.population.sortedByDescending { it.score }, this.generationCount)
    }

    @Synchronized
    fun play(settings: AlgoSettings): RunStats {
        val generationCount: Int
        val elapsedTime = measureTimeMillis {
            val algo = settings.toAlgo()
            generationCount = algo.search(settings.maxScore())
        }
        return RunStats(generationCount, elapsedTime)
    }


    private fun AlgoSettings.toAlgo() = GeneticAlgorithm(
        PUZZLES[puzzleId].toSurface(),
        PUZZLES[puzzleId].initialState,
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

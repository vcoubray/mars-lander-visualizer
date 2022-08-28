package services

import AlgoSettings
import PUZZLES
import PopulationResult
import PuzzleResult
import condigame.GeneticAlgorithm
import condigame.toSurface
import kotlin.system.measureTimeMillis

import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class AlgoService {
    var settings: AlgoSettings = Config.defaultSettings
    var algo = settings.toAlgo()
    var generationCount = 0


    @Synchronized
    fun reset(settings: AlgoSettings? = null): PopulationResult {
        settings?.let { this.settings = it }
        generationCount = 0
        algo = this.settings.toAlgo()
        return PopulationResult(emptyArray(), this.generationCount)
    }

    @Synchronized
    fun next(): PopulationResult {
        generationCount++
        algo.next()
        return PopulationResult(this.algo.population, this.generationCount)
    }

    @Synchronized
    fun play(settings: AlgoSettings): PuzzleResult {

        val generationCount: Int
        val elapsedTime = measureTimeMillis {
            val algo = settings.toAlgo()
            generationCount = algo.search(settings.maxScore())
        }
        return PuzzleResult(generationCount, elapsedTime)
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
        distanceWeight,
        crashSpeedWeight
    )


}



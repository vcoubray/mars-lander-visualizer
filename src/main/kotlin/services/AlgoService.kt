package services

import PUZZLES
import condigame.GeneticAlgorithm
import config.Config
import models.AlgoSettings
import models.PopulationResult
import models.PuzzleResult
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class AlgoService {
    var settings: AlgoSettings = Config.defaultSettings
    var algo = settings.toAlgo()
    var generationCount = 0


    fun reset(): PopulationResult {
        algo = settings.toAlgo()
        return PopulationResult(emptyArray(), this.generationCount)
    }

    fun updateSettings(settings: AlgoSettings): PopulationResult {
        this.settings = settings
        return reset()
    }

    fun next(): PopulationResult {
        generationCount++
        algo.next()
        return PopulationResult(this.algo.population, this.generationCount)
    }

    @OptIn(ExperimentalTime::class)
    fun play(id: Int): PuzzleResult {
        val generationCount: Int
        val elapsedTime = measureTime {

            generationCount = algo.search(settings.maxScore())
        }
        return PuzzleResult(id, generationCount, elapsedTime.inWholeMilliseconds)
    }


    private fun AlgoSettings.toAlgo() = GeneticAlgorithm(
        PUZZLES[puzzleId].surfacePath,
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


val algoService = AlgoService()
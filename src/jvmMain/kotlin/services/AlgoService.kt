package services

import AlgoSettings
import PUZZLES
import GenerationResult
import RunStats
import condigame.GeneticAlgorithm
import condigame.toSurface
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
        return GenerationResult(emptyArray(), this.generationCount)
    }

    @Synchronized
    fun next(): GenerationResult {
        generationCount++
        algo.next()
        return GenerationResult(this.algo.population, this.generationCount)
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
        distanceWeight,
        crashSpeedWeight
    )


}



package services

import condigame.GeneticAlgorithm
import config.Config
import models.AlgoSettings
import models.PopulationResult

class AlgoService {
    var algoSettings: AlgoSettings = Config.defaultSettings
    var algo = GeneticAlgorithm(algoSettings)
    var generationCount = 0


    fun reset() : PopulationResult {
        algo.init()
        return PopulationResult(emptyArray(), this.generationCount)
    }

    fun updateSettings(settings : AlgoSettings) : PopulationResult {
        algo.settings = settings
        return reset()
    }

    fun next(): PopulationResult {
        generationCount++
        algo.next()
        return PopulationResult(this.algo.population, this.generationCount)
    }

    fun next(step: Int): PopulationResult {
        repeat(step) {next()}
        return PopulationResult(this.algo.population, this.generationCount)
    }

}

val algoService = AlgoService()
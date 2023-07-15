package services

import AlgoSettings
import MarsSettings
import algorithm.Engine
import algorithm.GeneticAlgorithm
import algorithm.GeneticAlgorithmImpl
import condigame.MarsEngine
import condigame.toSurface
import java.lang.IllegalArgumentException


class AlgorithmFactory(
    private val puzzleService: PuzzleService,
) {

    fun fromSettings(settings: AlgoSettings): GeneticAlgorithm<*> {
        val engine: Engine<*> = when (settings.engineSettings) {
            is MarsSettings -> settings.engineSettings.toEngine()
            else -> throw IllegalArgumentException("Unknown engine settings type")
        }

        return GeneticAlgorithmImpl(
            engine = engine,
            chromosomeSize = settings.chromosomeSize,
            populationSize = settings.populationSize,
            mutationProbability = settings.mutationProbability,
            elitismPercent = settings.elitismPercent
        )
    }

    private fun MarsSettings.toEngine() = MarsEngine(
        puzzleService.getPuzzle(puzzleId)!!.toSurface(),
        puzzleService.getPuzzle(puzzleId)!!.initialState,
        speedMax,
        xSpeedWeight,
        ySpeedWeight,
        rotateWeight,
        distanceWeight
    )
}
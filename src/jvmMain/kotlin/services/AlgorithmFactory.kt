package services

import MarsEngineSettings
import SimulationSettings
import algorithm.Engine
import algorithm.GeneticAlgorithm
import algorithm.GeneticAlgorithmImpl
import condigame.MarsEngine
import condigame.toSurface


class AlgorithmFactory(
    private val puzzleService: PuzzleService,
) {

    fun  fromSettings(simulationSettings: SimulationSettings<*>): GeneticAlgorithm<*> {
        val engine: Engine<*> = when (simulationSettings.engineSettings) {
            is MarsEngineSettings -> simulationSettings.engineSettings.toEngine()
            else -> throw IllegalArgumentException("Unknown engine settings type")
        }

        return GeneticAlgorithmImpl(
            engine = engine,
            chromosomeSize = simulationSettings.globalSettings.chromosomeSize,
            populationSize = simulationSettings.globalSettings.populationSize,
            mutationProbability = simulationSettings.globalSettings.mutationProbability,
            elitismPercent = simulationSettings.globalSettings.elitismPercent
        )
    }

    private fun MarsEngineSettings.toEngine() = MarsEngine(
        puzzleService.getPuzzle(puzzleId)!!.toSurface(),
        puzzleService.getPuzzle(puzzleId)!!.initialState,
        speedMax,
        xSpeedWeight,
        ySpeedWeight,
        rotateWeight,
        distanceWeight
    )
}
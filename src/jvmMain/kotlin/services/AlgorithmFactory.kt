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
            chromosomeSize = simulationSettings.chromosomeSize,
            populationSize = simulationSettings.populationSize,
            mutationProbability = simulationSettings.mutationProbability,
            elitismPercent = simulationSettings.elitismPercent
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
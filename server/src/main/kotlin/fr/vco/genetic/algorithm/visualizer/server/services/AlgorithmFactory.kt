package fr.vco.genetic.algorithm.visualizer.server.services

import fr.vco.genetic.algorithm.visualizer.MarsEngineSettings
import fr.vco.genetic.algorithm.visualizer.SimulationSettings
import fr.vco.genetic.algorithm.visualizer.core.Engine
import fr.vco.genetic.algorithm.visualizer.core.GeneticAlgorithm
import fr.vco.genetic.algorithm.visualizer.core.GeneticAlgorithmImpl
import fr.vco.genetic.algorithm.visualizer.marslanding.MarsEngine
import fr.vco.genetic.algorithm.visualizer.marslanding.toSurface


class AlgorithmFactory(
    private val puzzleService: PuzzleService,
) {

    fun  fromSettings(simulationSettings: SimulationSettings<*>): GeneticAlgorithm<*> {
        val engine: Engine<*> = when (simulationSettings.engineSettings) {
            is MarsEngineSettings -> (simulationSettings.engineSettings as MarsEngineSettings).toEngine()
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
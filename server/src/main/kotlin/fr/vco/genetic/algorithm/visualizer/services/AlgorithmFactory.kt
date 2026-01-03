package fr.vco.genetic.algorithm.visualizer.services

import fr.vco.genetic.algorithm.visualizer.MarsEngineSettings
import fr.vco.genetic.algorithm.visualizer.SimulationSettings
import fr.vco.genetic.algorithm.visualizer.algorithm.Engine
import fr.vco.genetic.algorithm.visualizer.algorithm.GeneticAlgorithm
import fr.vco.genetic.algorithm.visualizer.algorithm.GeneticAlgorithmImpl
import fr.vco.genetic.algorithm.visualizer.codingame.MarsEngine
import fr.vco.genetic.algorithm.visualizer.codingame.toSurface


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
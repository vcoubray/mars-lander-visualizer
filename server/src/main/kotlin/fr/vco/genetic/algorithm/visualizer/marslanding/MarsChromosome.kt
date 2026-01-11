package fr.vco.genetic.algorithm.visualizer.marslanding

import fr.vco.genetic.algorithm.visualizer.core.Chromosome
import fr.vco.genetic.algorithm.visualizer.Action
import fr.vco.genetic.algorithm.visualizer.FitnessResult
import fr.vco.genetic.algorithm.visualizer.State

class MarsChromosome(val actions: Array<Action>) : Chromosome() {
    var normalizedScore = 0.0
    var cumulativeScore = 0.0
    var path = emptyList<Pair<Double, Double>>()
    var state: State = State()
    var fitnessResult: FitnessResult? = null
}
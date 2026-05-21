package fr.vco.genetic.algorithm.visualizer.marslanding

import fr.vco.genetic.algorithm.visualizer.core.Chromosome
import fr.vco.genetic.algorithm.visualizer.Action
import fr.vco.genetic.algorithm.visualizer.FitnessResult

class MarsChromosome(val actions: Array<Action>) : Chromosome() {
    var normalizedScore = 0.0
    var cumulativeScore = 0.0
    var path: List<Pair<Double, Double>> = emptyList()
    var state: MarsState = MarsState()
    var fitnessResult: FitnessResult? = null
}

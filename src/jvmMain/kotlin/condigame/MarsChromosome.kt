package condigame

import algorithm.Chromosome
import codingame.Action
import codingame.FitnessResult
import codingame.State

class MarsChromosome(val actions: Array<Action>) : Chromosome() {
    var normalizedScore = 0.0
    var cumulativeScore = 0.0
    var path = emptyList<Pair<Double, Double>>()
    var state: State = State()
    var fitnessResult: FitnessResult? = null
}
package codingame

import kotlinx.serialization.Serializable

@Serializable
class Chromosome(val actions: Array<Action>) {
    var score = 0.0
    var normalizedScore = 0.0
    var cumulativeScore = 0.0
    var path = emptyList<Pair<Double, Double>>()
    var state: State = State()
    var fitnessResult: FitnessResult? = null
}
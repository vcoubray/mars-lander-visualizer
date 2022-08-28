package codingame

import kotlinx.serialization.Serializable

@Serializable
class Chromosome(var id: Int, var actions: Array<Action>) {
    var score = 0.0
    var normalizedScore = 0.0
    var cumulativeScore = 0.0
    var path = emptyList<Pair<Double, Double>>()
    var state: State? = null
    var fitnessResult: FitnessResult? = null

    override fun equals(other: Any?): Boolean {
        return if (other is Chromosome) id == other.id
        else false
    }

    override fun hashCode(): Int {
        return id
    }
}
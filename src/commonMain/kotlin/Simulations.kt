import codingame.Action
import codingame.FitnessResult
import codingame.State
import kotlinx.serialization.Serializable

enum class SimulationStatus {
    PENDING,
    COMPLETE
}


@Serializable
data class SimulationResult(
    var id: Int,
    var status: SimulationStatus = SimulationStatus.PENDING,
    var duration: Long = 0,
    var bestScore: Double = 0.0,
    var generations: List<Generation> = emptyList(),
)

@Serializable
data class SimulationSummary(
    var id: Int,
    var status: SimulationStatus = SimulationStatus.PENDING,
    var duration: Long = 0,
    var bestScore: Double = 0.0,
    var generationCount: Int,
)


@Serializable
data class Generation(
    val population: List<IndividualResult>,
) {
    val best = population.takeIf { it.isNotEmpty() }?.map { it.score }?.maxOrNull() ?: 0.0
    val mean = population.takeIf { it.isNotEmpty() }?.map { it.score }?.average() ?: 0.0
}

@Serializable
data class GenerationSummary(
    val populationSize: Int,
    val best: Double,
    val mean: Double,
)

@Serializable
class IndividualResult(
    val actions: List<Action>,
    val path: List<Pair<Double, Double>>,
    val state: State,
    val score: Double = 0.0,
    val normalizedScore: Double = 0.0,
    val cumulativeScore: Double = 0.0,
    val fitnessResult: FitnessResult? = null,
)
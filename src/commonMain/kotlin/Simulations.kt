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
    var simulationSettings: SimulationSettings<*>,
    var status: SimulationStatus = SimulationStatus.PENDING,
    var duration: Long = 0,
    var bestScore: Double = 0.0,
    var generations: List<GenerationResult> = emptyList(),
)

@Serializable
class GenerationResult(
    val population: List<MarsChromosomeResult>,
) {
    val best = population.takeIf { it.isNotEmpty() }?.maxOfOrNull { it.score } ?: 0.0
    val mean = population.takeIf { it.isNotEmpty() }?.map { it.score }?.average() ?: 0.0
}

@Serializable
class MarsChromosomeResult(
    val id: Int,
    val actions: List<Action>,
    val path: List<Pair<Double, Double>>,
    val state: State,
    val score: Double = 0.0,
    val normalizedScore: Double = 0.0,
    val cumulativeScore: Double = 0.0,
    val fitnessResult: FitnessResult? = null,
)

@Serializable
data class SimulationSummary(
    var id: Int,
    var simulationSettings: SimulationSettings<*>,
    var status: SimulationStatus = SimulationStatus.PENDING,
    var duration: Long = 0,
    var bestScore: Double = 0.0,
    var generationCount: Int,
)

@Serializable
data class GenerationSummary(
    val populationSize: Int,
    val best: Double,
    val mean: Double,
)

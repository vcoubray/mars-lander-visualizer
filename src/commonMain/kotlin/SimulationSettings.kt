import kotlinx.serialization.Serializable

enum class LimitType(val label: String) {
    TIME("Time (ms)"), SCORE("Score")
}

@Serializable
data class SimulationSettings<T : EngineSettings>(
    var limitType: LimitType,
    var limitValue: Int,
    var chromosomeSize: Int,
    var populationSize: Int,
    var mutationProbability: Double,
    var elitismPercent: Double,
    val engineSettings: T,
)

@Serializable
sealed interface EngineSettings {
    fun maxScore(): Double
}

typealias MarsSettings = SimulationSettings<MarsEngineSettings>
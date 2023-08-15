import kotlinx.serialization.Serializable

enum class LimitType(val label: String) {
    TIME("Time (ms)"), SCORE("Score")
}

@Serializable
data class SimulationSettings<T : EngineSettings>(
    val globalSettings: GlobalSettings,
    val engineSettings: T,
)


@Serializable
data class GlobalSettings (
    var limitType: LimitType,
    var limitValue: Int,
    var chromosomeSize: Int,
    var populationSize: Int,
    var mutationProbability: Double,
    var elitismPercent: Double
)

@Serializable
sealed interface EngineSettings {
    fun maxScore(): Double
}

typealias MarsSettings = SimulationSettings<MarsEngineSettings>
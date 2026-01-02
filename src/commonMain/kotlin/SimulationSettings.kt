import kotlinx.serialization.Serializable

interface LabeledEnum {
    val label: String
}


enum class LimitType(override val label: String): LabeledEnum {
    TIME("Time (ms)"), SCORE("Score");

}

enum class SelectionType(override val label: String): LabeledEnum {
    RANDOM("Random")
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
    var elitismPercent: Double,
    var selectionType: SelectionType
)

@Serializable
sealed interface EngineSettings {
    fun maxScore(): Double
}

typealias MarsSettings = SimulationSettings<MarsEngineSettings>
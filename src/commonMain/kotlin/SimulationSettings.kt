import kotlinx.serialization.Serializable

@Serializable
data class SimulationSettings<T: EngineSettings>(
    var chromosomeSize: Int,
    var populationSize: Int,
    var mutationProbability: Double,
    var elitismPercent: Double,
    val engineSettings: T
)

@Serializable
sealed interface EngineSettings {
    fun maxScore(): Double
}

typealias MarsSettings = SimulationSettings<MarsEngineSettings>
import kotlinx.serialization.Serializable

@Serializable
data class AlgoSettings(
    var chromosomeSize: Int,
    var populationSize: Int,
    var mutationProbability: Double,
    var elitismPercent: Double,
    val engineSettings: EngineSettings,
)

sealed interface EngineSettings {
    fun maxScore(): Double
}

@Serializable
data class MarsSettings(
    var puzzleId: Int,
    var speedMax: Double,
    var xSpeedWeight: Double,
    var ySpeedWeight: Double,
    var rotateWeight: Double,
    var distanceWeight: Double,
    var crashSpeedWeight: Double,
) : EngineSettings {
    override fun maxScore() = xSpeedWeight + ySpeedWeight + rotateWeight + distanceWeight
}

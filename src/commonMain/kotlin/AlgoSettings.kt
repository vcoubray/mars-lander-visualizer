import kotlinx.serialization.Serializable

@Serializable
data class AlgoSettings(
    var chromosomeSize: Int,
    var populationSize: Int,
    var mutationProbability: Double,
    var elitismPercent: Double,
    var puzzleId: Int,
    var speedMax: Double,
    var xSpeedWeight: Double,
    var ySpeedWeight: Double,
    var rotateWeight: Double,
    var distanceWeight: Double,
    var crashSpeedWeight: Double
) {
    fun maxScore() = xSpeedWeight + ySpeedWeight + rotateWeight + distanceWeight
}
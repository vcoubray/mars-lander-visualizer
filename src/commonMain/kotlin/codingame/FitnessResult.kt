package codingame

import kotlinx.serialization.Serializable

@Serializable
data class FitnessResult(
    var distance: Double,
    var xSpeedOverflow: Double,
    var ySpeedOverflow: Double,
    var rotateOverflow: Int,
    var status: CrossingEnum
)
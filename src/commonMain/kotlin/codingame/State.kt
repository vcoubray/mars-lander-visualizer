package codingame

import kotlinx.serialization.Serializable

@Serializable
data class State(
    var x: Double,
    var y: Double,
    var xSpeed: Double,
    var ySpeed: Double,
    var fuel: Int,
    var rotate: Int,
    var power: Int
) {
    val path = mutableListOf(x to y)
}
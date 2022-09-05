package codingame

import kotlinx.serialization.Serializable

@Serializable
data class State(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var xSpeed: Double = 0.0,
    var ySpeed: Double = 0.0,
    var fuel: Int = 0,
    var rotate: Int = 0,
    var power: Int = 0,
) {
    var path = mutableListOf(x to y)

    fun loadFrom(state: State, withPath: Boolean = false) {
        x = state.x
        y = state.y
        xSpeed = state.xSpeed
        ySpeed = state.ySpeed
        fuel = state.fuel
        rotate = state.rotate
        power = state.power
        if (withPath) {
            path = state.path.toMutableList()
        } else {
            path.clear()
            path.add(x to y)
        }
    }
}
package codingame

import kotlinx.serialization.Serializable

@Serializable
data class Action(var rotate: Int, var power: Int) {

    fun randomize() {
        rotate = ROTATE_RANGE.random()
        power = POWER_RANGE.random()
    }

    override fun toString() = "$rotate $power"
}
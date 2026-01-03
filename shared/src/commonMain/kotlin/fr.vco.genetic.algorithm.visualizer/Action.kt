package fr.vco.genetic.algorithm.visualizer

import fr.vco.genetic.algorithm.visualizer.codingame.POWER_RANGE
import fr.vco.genetic.algorithm.visualizer.codingame.ROTATE_RANGE
import kotlinx.serialization.Serializable

@Serializable
data class Action(var rotate: Int, var power: Int) {

    fun randomize() {
        rotate = ROTATE_RANGE.random()
        power = POWER_RANGE.random()
    }

    override fun toString() = "$rotate $power"
}
package fr.vco.genetic.algorithm.visualizer.marslanding

import fr.vco.genetic.algorithm.visualizer.Action
import fr.vco.genetic.algorithm.visualizer.CrossingEnum
import fr.vco.genetic.algorithm.visualizer.FitnessResult
import fr.vco.genetic.algorithm.visualizer.State
import fr.vco.genetic.algorithm.visualizer.codingame.MARS_GRAVITY
import fr.vco.genetic.algorithm.visualizer.codingame.ROTATE_RANGE
import kotlin.math.absoluteValue
import kotlin.math.max


data class MarsState(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var xSpeed: Double = 0.0,
    var ySpeed: Double = 0.0,
    var fuel: Int = 0,
    var rotate: Int = 0,
    var power: Int = 0,
) {
    fun loadFrom(state: State) {
        x = state.x
        y = state.y
        xSpeed = state.xSpeed
        ySpeed = state.ySpeed
        fuel = state.fuel
        rotate = state.rotate
        power = state.power
    }

    fun loadFrom(state: MarsState) {
        x = state.x
        y = state.y
        xSpeed = state.xSpeed
        ySpeed = state.ySpeed
        fuel = state.fuel
        rotate = state.rotate
        power = state.power
    }
}

data class MarsSimulationResult(
    val finalState: MarsState = MarsState(),
    val path: MutableList<Pair<Double, Double>> = mutableListOf(),
    var fitness: FitnessResult? = null,
)

// The shared `workingState` buffer is fine for the current single-threaded GA execution.
// If we ever run multiple GAs concurrently (e.g. parallel benchmarks), promote MarsSimulator
// to a class instantiated per MarsEngine.
object MarsSimulator {
    private val workingState = MarsState()

    fun play(
        initialState: MarsState,
        actions: Array<Action>,
        surface: Surface,
        result: MarsSimulationResult,
    ): MarsSimulationResult {
        workingState.loadFrom(initialState)
        result.path.clear()
        result.path.add(workingState.x to workingState.y)

        var lastX = workingState.x
        var lastY = workingState.y
        var lastRotate = workingState.rotate
        var distance = -1.0

        var i = 0
        while (i < actions.size) {
            lastX = workingState.x
            lastY = workingState.y
            lastRotate = workingState.rotate
            workingState.play(actions[i])
            result.path.add(workingState.x to workingState.y)
            distance = surface.cross(lastX, lastY, workingState.x, workingState.y)
            if (distance >= 0 || workingState.x !in surface.widthRange || workingState.y !in surface.heightRange) {
                break
            }
            i++
        }

        var status = CrossingEnum.NOPE
        if (distance >= 0.0) {
            if (distance == 0.0) {
                if (lastRotate in ROTATE_RANGE) workingState.rotate = 0
                actions[i].rotate = -lastRotate
                status = CrossingEnum.LANDING_ZONE
            } else {
                status = CrossingEnum.CRASH
            }
        } else if (workingState.x !in surface.widthRange || workingState.y !in surface.widthRange) {
            distance = surface.distanceMax
        } else {
            for (segment in surface.segments) {
                if (workingState.x in segment.xRange) {
                    val crossingY = segment.start.y + (workingState.x - segment.start.x) / segment.vx * segment.vy
                    if (crossingY < workingState.y) {
                        val yDist = workingState.y - crossingY
                        distance = boundedValue(
                            yDist * yDist + segment.distanceToLanding(workingState.x),
                            0.0,
                            surface.distanceMax,
                        )
                        break
                    }
                }
            }
        }

        result.finalState.loadFrom(workingState)
        result.fitness = FitnessResult(
            distance = distance,
            xSpeedOverflow = max(workingState.xSpeed.absoluteValue - 20, 0.0),
            ySpeedOverflow = max(workingState.ySpeed.absoluteValue - 40, 0.0),
            rotateOverflow = max(lastRotate.absoluteValue - 10, 0),
            status = status,
        )
        return result
    }


    private fun MarsState.play(action: Action) {
        this.power = boundedValue(this.power + action.power, 0, 4)
        this.rotate = boundedValue(this.rotate + action.rotate, -90, 90)

        val newXSpeed = (this.xSpeed + this.power * X_VECTOR[this.rotate]!!)
        val newYSPeed = (this.ySpeed + this.power * Y_VECTOR[this.rotate]!!) - MARS_GRAVITY

        this.x += (this.xSpeed + newXSpeed) * 0.5
        this.y += (this.ySpeed + newYSPeed) * 0.5

        this.xSpeed = newXSpeed
        this.ySpeed = newYSPeed

        this.fuel -= power
    }
}

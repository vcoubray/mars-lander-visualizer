package condigame

import boundedValue
import kotlin.math.absoluteValue
import kotlin.math.max

enum class CrossingEnum {
    NOPE, CRASH, LANDING_ZONE
}

data class Result(
    var distance: Double,
    var xSpeedOverflow: Double,
    var ySpeedOverflow: Double,
    var rotateOverflow: Int,
    var status: CrossingEnum
)

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

    fun play(action: Action) {

        this.power = boundedValue(this.power + action.power, 0, 4)
        this.rotate = boundedValue(this.rotate + action.rotate, -90, 90)

        val newXSpeed = (this.xSpeed + this.power * X_VECTOR[this.rotate]!!)
        val newYSPeed = (this.ySpeed + this.power * Y_VECTOR[this.rotate]!!) - MARS_GRAVITY

        this.x += (this.xSpeed + newXSpeed) * 0.5
        this.y += (this.ySpeed + newYSPeed) * 0.5

        this.xSpeed = newXSpeed
        this.ySpeed = newYSPeed

        this.fuel -= power
        path.add(this.x to this.y)
    }

    fun play(actions: Array<Action>, surface: Surface): Result {

        var lastX: Double
        var lastY: Double
        var lastRotate= rotate
        var crossing: Pair<Segment, Point>? = null
        for (action in actions) {
            lastX = x
            lastY = y
            lastRotate = rotate
            play(action)
            crossing = surface.cross(Segment(Point(lastX, lastY), Point(x, y)))
            if (crossing != null || x.toInt() !in (0..surface.width) || y.toInt() !in (0..surface.height)) {
                break
            }
        }

        val xSpeedDist = max(xSpeed.absoluteValue - 20, 0.0)
        val ySpeedDist = max(ySpeed.absoluteValue - 40, 0.0)
        var status = CrossingEnum.NOPE

        val distance = if (crossing != null) {
            status = if (crossing.first.isLandingZone) {
                if (lastRotate in (-15..15)) rotate = 0
                CrossingEnum.LANDING_ZONE
            } else {
                CrossingEnum.CRASH
            }
            crossing.first.distanceToLanding(crossing.second.x)
        } else {
            val projection = Segment(Point(x, y), Point(x, 0.0))
            val crossings = surface.segments
                .mapNotNull { segment -> segment.cross(projection)?.let { segment to it } }
            var yCrossing = 0.0
            crossing = null
            for (cross in crossings) {
                val (_, point) = cross
                if (point.y < y && point.y > yCrossing) {
                    yCrossing = point.y
                    crossing = cross
                }
            }
            boundedValue(
                y - yCrossing + (crossing?.first?.distanceToLanding(crossing.second.x) ?: surface.distanceMax),
                0.0,
                surface.distanceMax
            )
        }

        return Result(
            distance = distance,
            xSpeedOverflow = xSpeedDist,
            ySpeedOverflow = ySpeedDist,
            rotateOverflow = max(lastRotate.absoluteValue - 10, 0),
            status = status
        )

    }
}
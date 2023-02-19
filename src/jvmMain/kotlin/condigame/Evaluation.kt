package condigame

import boundedValue
import codingame.*
import kotlin.math.absoluteValue
import kotlin.math.max

fun State.play(action: Action) {

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

fun State.play(actions: Array<Action>, surface: Surface): FitnessResult {

    var lastX: Double
    var lastY: Double
    var lastRotate = rotate
    var distance: Double = -1.0

    var i = 0
    for (action in actions) {
        lastX = x
        lastY = y
        lastRotate = rotate
        play(action)
        distance = surface.cross(lastX, lastY, x, y)
        if (distance >= 0 || x !in surface.widthRange || y !in surface.heightRange) {
            break
        }
        i++
    }

    var status = CrossingEnum.NOPE

    if (distance >= 0.0) {
        if (distance == 0.0) {
            if (lastRotate in ROTATE_RANGE) rotate = 0
            actions[i].rotate = -lastRotate
            status = CrossingEnum.LANDING_ZONE
        } else {
            status = CrossingEnum.CRASH
        }
    } else if (x !in surface.widthRange || y !in surface.widthRange) {
        distance = surface.distanceMax
    } else {
        for (segment in surface.segments) {
            if (x in segment.xRange) {
                val crossingY = segment.start.y + (x - segment.start.x) / segment.vx * segment.vy
                if (crossingY < y) {
                    val yDist = y - crossingY
                    distance = boundedValue(
                        yDist * yDist + segment.distanceToLanding(x),
                        0.0,
                        surface.distanceMax
                    )
                    break
                }
            }
        }
    }

//    val distance = if (crossing != null) {
//        status = if (crossing.first.isLandingZone) {
//            if (lastRotate in ROTATE_RANGE) rotate = 0
//            actions[i].rotate = -lastRotate
//            CrossingEnum.LANDING_ZONE
//        } else {
//            CrossingEnum.CRASH
//        }
//        crossing.first.distanceToLanding(crossing.second.x)
//    } else {
//
//
//        }
//        val crossings = surface.segments
//            .mapNotNull { segment -> segment.cross(x,y,x,0.0)?.let { segment to it } }
//            .first{}
//        var yCrossing = 0.0
//        crossing = null
//        for (cross in crossings) {
//            val (_, point) = cross
//            if (point.y < y && point.y > yCrossing) {
//                yCrossing = point.y
//                crossing = cross
//            }
//        }
//        boundedValue(
//            y - yCrossing + (crossing?.first?.distanceToLanding(crossing.second.x) ?: surface.distanceMax),
//            0.0,
//            surface.distanceMax
//        )
//    }

    val xSpeedDist = max(xSpeed.absoluteValue - 20, 0.0)
    val ySpeedDist = max(ySpeed.absoluteValue - 40, 0.0)

    return FitnessResult(
        distance = distance,
        xSpeedOverflow = xSpeedDist,
        ySpeedOverflow = ySpeedDist,
        rotateOverflow = max(lastRotate.absoluteValue - 10, 0),
        status = status
    )

}
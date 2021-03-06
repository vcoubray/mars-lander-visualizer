import csstype.Height
import kotlin.math.*

const val MARS_GRAVITY = 3.711

fun toRadians(value: Double) = value * PI / 180
val Y_VECTOR = (-90..90).associateWith { cos(toRadians(it.toDouble())) }
val X_VECTOR = (-90..90).associateWith { -sin(toRadians(it.toDouble())) }

fun boundedValue(value: Int, min: Int, max: Int) = when {
    value <= min -> min
    value >= max -> max
    else -> value
}

//fun generateAction(rotate: Int, power: Int): Action {

//    val minPower =  boundedValue ( 0 - power, -1,0)
//    val maxPower = boundedValue (4 - power , 0, 1)
//    val minRotate = boundedValue ( -90 - rotate, -15,0)
//    val maxRotate = boundedValue ( 90- rotate , 0,15)
//    return Action(
//        boundedValue(rotate + (-15..15).random(), -90, 90),
//        boundedValue(power + (-1..1).random(), 0, 4)
//            boundedValue(rotate + (minRotate..maxRotate).random(), -90, 90),
//        boundedValue(power + (minPower..maxPower).random(), 0, 4)
//    )
//}

fun generateAction(): Action {
    return Action(
        (-15..15).random(),
        (-1..1).random()
    )
}

data class Action(val rotate: Int, val power: Int) {
    override fun toString() = "$rotate $power"
}

enum class CrossingEnum {
    NOPE, CRASH, LANDING_ZONE
}

data class Segment(val start: Pair<Double, Double>, val end: Pair<Double, Double>)

data class Surface(
    val height: Int,
    val width: Int,
    val segments: List<Segment>
) {

    val landingZoneY: Double
    val landingZoneX: Pair<Double, Double>

    init {
        val landingZone = segments.first { it.start.second == it.end.second }
        landingZoneY = landingZone.start.second
        landingZoneX = landingZone.start.first to landingZone.end.first
    }

    private fun cross(s1: Segment, s2: Segment): Boolean {
        val s1x = s1.end.first - s1.start.first
        val s1y = s1.end.second - s1.start.second
        val s2x = s2.end.first - s2.start.first
        val s2y = s2.end.second - s2.start.second

        val s =
            (-s1y * (s1.start.first - s2.start.first) + s1x * (s1.start.second - s2.start.second)) / (-s2x * s1y + s1x * s2y)
        val t =
            (-s2y * (s1.start.first - s2.start.first) + s2x * (s1.start.second - s2.start.second)) / (-s2x * s1y + s1x * s2y)

        return (s in 0.0..1.0 && t in 0.0..1.0)
    }

    fun cross(path: Segment): CrossingEnum {
        val (x,y) = path.end
        if (x.toInt() !in (0..width) || y.toInt() !in (0..height))
            return CrossingEnum.NOPE
        for (segment in segments) {
            if (cross(segment, path)) {
                return if (segment.start.second == segment.end.second) CrossingEnum.LANDING_ZONE
                else CrossingEnum.CRASH
            }
        }
        return CrossingEnum.NOPE
    }

    fun distanceToLandingZone(x: Double, y: Double): Double {
        return when {
            x < landingZoneX.first -> sqrt((x - landingZoneX.first).pow(2) + (y - landingZoneY).pow(2))
            x > landingZoneX.second -> sqrt((x - landingZoneX.second).pow(2) + (y - landingZoneY).pow(2))
            else -> abs(y - landingZoneY)
        }
    }

    fun distanceXToLandingZone(x: Double): Double {
        return when {
            x < landingZoneX.first -> landingZoneX.first - x
            x > landingZoneX.second -> x - landingZoneX.second
            else -> 0.0
        }
    }


//    fun distanceYtoLandingZone(y:Double) : Double{
//        return when {
//            x < landingZoneX.first -> landingZoneX.first - x
//            x > landingZoneX.second -> x - landingZoneX.second
//            else -> 0.0
//        }
//    }
}

data class State(
    var x: Double,
    var y: Double,
    var xSpeed: Double,
    var ySpeed: Double,
    var fuel: Int,
    var rotate: Int,
    var power: Int,

) {

    val path = mutableListOf(x to y)
    var status = CrossingEnum.NOPE
    fun play(action: Action) {

        this.power = boundedValue(this.power + action.power, 0, 4)
        this.rotate = boundedValue(this.rotate + action.rotate, -90, 90)
//        this.power = action.power
//        this.rotate = action.rotate

        val newXSpeed = (this.xSpeed + this.power * X_VECTOR[this.rotate]!!)
        val newYSPeed = (this.ySpeed + this.power * Y_VECTOR[this.rotate]!!) - MARS_GRAVITY

        this.x += (this.xSpeed + newXSpeed) * 0.5
        this.y += (this.ySpeed + newYSPeed) * 0.5

        this.xSpeed = newXSpeed
        this.ySpeed = newYSPeed

        this.fuel -= power
        path.add(this.x to this.y)
    }

    fun play(actions: Array<Action>, surface: Surface): Double {

        val distanceMax = 10000
        var lastX = x
        var lastY = y
        var crossing = CrossingEnum.NOPE
        for (action in actions) {
            lastX = x
            lastY = y
            play(action)
            crossing = surface.cross(Segment(lastX to lastY, x to y))
            if (crossing != CrossingEnum.NOPE) {
                break
            }
        }

        val xSpeedDist = max(xSpeed.absoluteValue-20,0.0)
        val ySpeedDist = max( ySpeed.absoluteValue-40, 0.0)
        val rotateDist = max(rotate.absoluteValue-10,0)

        val rotateMax = 80
        this.status = crossing
        if (crossing != CrossingEnum.LANDING_ZONE) {
            return (distanceMax - surface.distanceToLandingZone(x, y)) / distanceMax * 100 - (xSpeedDist + ySpeedDist) * 0.1
            //  return (distanceMax - surface.distanceXToLandingZone(x)) / distanceMax * 50
        } else {
            return 200.0 - ((rotateDist * 100.0 / rotateMax) + (xSpeedDist + ySpeedDist))

//            if (crossing == CrossingEnum.CRASH) {
//                return (distanceMax - surface.distanceToLandingZone(lastX, lastY)) / distanceMax * 50
////                return (distanceMax - surface.distanceXToLandingZone(lastX)) / distanceMax * 50
//            } else {
//                if (xSpeed in (-20.0..20.0) && ySpeed in (0.0..-40.0) && rotate in (-10..10)) {
//                    return 100.0
//                } else {
//
//
//                    return 50 + (1090 - (xSpeed.absoluteValue + ySpeed.absoluteValue + rotate.absoluteValue)) / 1090 * 50
//                }
//            }

        }
    }

    fun score(surface: Surface) : Double {
        val dist = surface.distanceToLandingZone(x,y)
        val xSpeedDist = max(xSpeed.absoluteValue-20,0.0)
        val ySpeedDist = max( ySpeed.absoluteValue-40, 0.0)
        val rotateDist = max(rotate.absoluteValue-10,0)

        return 1/dist +  1/xSpeed + 1/ySpeed + 1/rotateDist
    }
}


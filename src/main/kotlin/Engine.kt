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

fun boundedValue(value: Double, min: Double, max: Double) = when {
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


data class Point(val x: Double, val y: Double)

data class Segment(val start: Point, val end: Point) {
    val length = sqrt((start.x - end.x).pow(2) + (start.y - end.y).pow(2))
    var isLandingZone: Boolean = false
    var distanceToLanding = 0.0
    lateinit var proportion: (Segment, Double) -> Double

    fun distanceToLanding(x: Double): Double {
        return if (isLandingZone) 0.0
        else distanceToLanding + (proportion(this, x) * length)
    }

    fun distanceForProjection(x: Double, y: Double): Double {
        return distanceToLanding(x) + (y - start.y)
    }

    fun getYProjection(x: Double): Double {
        return (start.x - x) / (start.x - end.x) * (start.y - end.y) + end.y
    }

    fun getProjection(x: Double, y: Double): Pair<Double, Double> {
        val yp = (start.x - x) / (start.x - end.x) * (start.y - end.y) + end.y
        val xp = (start.y - y) / (start.y - end.y) * (start.x - end.x) + end.x
        return xp to yp
    }

}

data class Surface(
    val height: Int,
    val width: Int,
    val segments: List<Segment>
) {

    val landingZoneY: Double
    val landingZoneX: Pair<Double, Double>
    var distanceMax: Double = 0.0

    init {

        val landingZoneIndex = segments.indexOfFirst { it.start.y == it.end.y }
        segments[landingZoneIndex].isLandingZone = true
        landingZoneY = segments[landingZoneIndex].start.y
        landingZoneX = segments[landingZoneIndex].start.x to segments[landingZoneIndex].end.x


        var sum = 0.0
        for (i in landingZoneIndex - 1 downTo 0) {
            segments[i].distanceToLanding = sum
            segments[i].proportion =
                { segment, x -> (x - segment.end.x) / (segment.start.x - segment.end.x) }
            sum += segments[i].length

        }
        distanceMax = sum
        sum = 0.0
        for (i in landingZoneIndex + 1 until segments.size) {
            segments[i].distanceToLanding = sum
            segments[i].proportion =
                { segment, x -> (x - segment.start.x) / (segment.end.x - segment.start.x) }
            sum += segments[i].length
        }

        if (sum > distanceMax) {
            distanceMax = sum
        }

    }

    fun cross(s1: Segment, s2: Segment): Point? {
        val s1x = s1.end.x - s1.start.x
        val s1y = s1.end.y - s1.start.y
        val s2x = s2.end.x - s2.start.x
        val s2y = s2.end.y - s2.start.y

        if ((s2x * s1y == s1x * s2y)) return null

        val s =
            (-s1y * (s1.start.x - s2.start.x) + s1x * (s1.start.y - s2.start.y)) / (-s2x * s1y + s1x * s2y)
        val t =
            (-s2y * (s1.start.x - s2.start.x) + s2x * (s1.start.y - s2.start.y)) / (-s2x * s1y + s1x * s2y)

        if (s in 0.0..1.0 && t in 0.0..1.0) {
            return Point(
                s1.start.x + (t * s1x),
                s1.start.y + (t * s1y)
            )
        }
        return null
    }

    fun cross(path: Segment): Pair<Segment, Point>? {
        for (segment in segments) {
            cross(segment,path)?.let{
                return segment to it
            }
        }
        return null
    }


    fun distanceToLandingZone(x: Double, y: Double): Double {
        return when {
            x < landingZoneX.first -> sqrt((x - landingZoneX.first).pow(2) + (y - landingZoneY).pow(2))
            x > landingZoneX.second -> sqrt((x - landingZoneX.second).pow(2) + (y - landingZoneY).pow(2))
            else -> abs(y - landingZoneY)
        }
    }
}

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
    var status = CrossingEnum.NOPE
    var normalizedDistance = 0.0
    var normalizedSpeed = 0.0
    var normalizedRotate = 0.0

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

    fun play(actions: Array<Action>, surface: Surface, settings: AlgoSettings): Double {

        var lastX: Double
        var lastY: Double
        var crossing: Pair<Segment,Point>? = null
        for (action in actions) {
            lastX = x
            lastY = y
            play(action)
            crossing = surface.cross(Segment(Point(lastX, lastY), Point(x, y)))
            if (crossing != null || x.toInt() !in (0..surface.width) || y.toInt() !in (0..surface.height)) {
                break
            }
        }

        val xSpeedDist = max(xSpeed.absoluteValue - 20, 0.0)
        val ySpeedDist = max(ySpeed.absoluteValue - 40, 0.0)
        val rotateDist = max(rotate.absoluteValue - 10, 0)
        val rotateMax = 80

        val speedMax = settings.speedMax
        normalizedSpeed = (speedMax - xSpeedDist - ySpeedDist) * 100.0 / speedMax
        normalizedRotate = (rotateMax - rotateDist) * 100.0 / rotateMax


        if ( crossing?.first?.isLandingZone == true) {
            status = CrossingEnum.LANDING_ZONE
            return 100.0 + normalizedRotate * 0.5 + normalizedSpeed * 0.5
        }

        val distance = if (crossing != null) {
            status = CrossingEnum.CRASH
            crossing.first.distanceToLanding(crossing.second.x)
        } else {
            val projection = Segment(Point(x, y), Point(x, 0.0))
            val crossings = surface.segments
                .mapNotNull { segment -> surface.cross(segment, projection)?.let { segment to it } }
            var yCrossing = 0.0
            crossing = null
            for (cross in crossings) {
                val (_, point ) = cross
                console.log(point)
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

        normalizedDistance = (surface.distanceMax - distance) * 100 / surface.distanceMax

        return normalizedDistance * (1-settings.speedWeight) + normalizedSpeed * settings.speedWeight
    }
}


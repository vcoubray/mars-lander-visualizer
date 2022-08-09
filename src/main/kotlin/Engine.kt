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
            cross(segment, path)?.let {
                return segment to it
            }
        }
        return null
    }
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
//    var status = CrossingEnum.NOPE
//    var normalizedDistance = 0.0
//    var normalizedSpeed = 0.0
//    var normalizedRotate = 0.0

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
                .mapNotNull { segment -> surface.cross(segment, projection)?.let { segment to it } }
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
            rotateOverflow = max(lastRotate.absoluteValue - 10,0),
            status = status
        )

    }
}


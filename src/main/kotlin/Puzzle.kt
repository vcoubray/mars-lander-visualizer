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

        val distanceMax = surface.distanceToLandingZone(x, y)
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

        if (crossing == CrossingEnum.NOPE) {
            return (distanceMax - surface.distanceToLandingZone(x, y)) / distanceMax * 50
            //  return (distanceMax - surface.distanceXToLandingZone(x)) / distanceMax * 50
        } else {
            if (crossing == CrossingEnum.CRASH) {
                return (distanceMax - surface.distanceToLandingZone(lastX, lastY)) / distanceMax * 50
//                return (distanceMax - surface.distanceXToLandingZone(lastX)) / distanceMax * 50
            } else {
                if (xSpeed in (-20.0..20.0) && ySpeed in (0.0..-40.0) && rotate in (-10..10)) {
                    return 100.0
                } else {
                    return 50 + (1090 - (xSpeed.absoluteValue + ySpeed.absoluteValue + rotate.absoluteValue)) / 1090 * 50
                }
            }

        }
    }
}

data class Puzzle(
    val id: Int,
    val title: String,
    val surface: String,
    val initialState: State
) {
    fun getSurfacePath() = Surface(
        surface.split(" ")
            .map { it.toDouble() }
            .chunked(2)
            .map { (a, b) -> a to b }
            .windowed(2)
            .map { (a, b) -> Segment(a, b) }
    )
}

val PUZZLES = listOf(
    Puzzle(
        1,
        "Facile à droite",
        "0.0 100.0 1000.0 500.0 1500.0 1500.0 3000.0 1000.0 4000.0 150.0 5500.0 150.0 6999.0 800.0",
        State(x = 2500.0, y = 2700.0, xSpeed = 0.0, ySpeed = 0.0, fuel = 550, rotate = 0, power = 0)
    ),
    Puzzle(
        2,
        "Vitesse d'entrée, bon côté",
        "0.0 100.0 1000.0 500.0 1500.0 100.0 3000.0 100.0 3500.0 500.0 3700.0 200.0 5000.0 1500.0 5800.0 300.0 6000.0 1000.0 6999.0 2000.0",
        State(x = 6500.0, y = 2800.0, xSpeed = -100.0, ySpeed = 0.0, fuel = 600, rotate = 90, power = 0)
    ),
    Puzzle(
        3,
        "Vitesse d'entrée, mauvais côté",
        "0.0 100.0 1000.0 500.0 1500.0 1500.0 3000.0 1000.0 4000.0 150.0 5500.0 150.0 6999.0 800.0",
        State(x = 6500.0, y = 2800.0, xSpeed = -90.0, ySpeed = 0.0, fuel = 750, rotate = 90, power = 0)
    ),
    Puzzle(
        4,
        "Gorge profonde",
        "0.0 1000.0 300.0 1500.0 350.0 1400.0 500.0 2000.0 800.0 1800.0 1000.0 2500.0 1200.0 2100.0 1500.0 2400.0 2000.0 1000.0 2200.0 500.0 2500.0 100.0 2900.0 800.0 3000.0 500.0 3200.0 1000.0 3500.0 2000.0 3800.0 800.0 4000.0 200.0 5000.0 200.0 5500.0 1500.0 6999.0 2800.0",
        State(x = 500.0, y = 2700.0, xSpeed = 100.0, ySpeed = 0.0, fuel = 800, rotate = -90, power = 0)
    ),
    Puzzle(
        5,
        "Haut plateau",
        "0.0 1000.0 300.0 1500.0 350.0 1400.0 500.0 2100.0 1500.0 2100.0 2000.0 200.0 2500.0 500.0 2900.0 300.0 3000.0 200.0 3200.0 1000.0 3500.0 500.0 3800.0 800.0 4000.0 200.0 4200.0 800.0 4800.0 600.0 5000.0 1200.0 5500.0 900.0 6000.0 500.0 6500.0 300.0 6999.0 500.0",
        State(x = 6500.0, y = 2700.0, xSpeed = -50.0, ySpeed = 0.0, fuel = 1000, rotate = 90, power = 0)
    ),
    Puzzle(
        6,
        "Grotte, bon côté",
        "0.0 450.0 300.0 750.0 1000.0 450.0 1500.0 650.0 1800.0 850.0 2000.0 1950.0 2200.0 1850.0 2400.0 2000.0 3100.0 1800.0 3150.0 1550.0 2500.0 1600.0 2200.0 1550.0 2100.0 750.0 2200.0 150.0 3200.0 150.0 3500.0 450.0 4000.0 950.0 4500.0 1450.0 5000.0 1550.0 5500.0 1500.0 6000.0 950.0 6999.0 1750.0",
        State(x = 6500.0, y = 2600.0, xSpeed = -20.0, ySpeed = 0.0, fuel = 1000, rotate = 45, power = 0)
    ),
    Puzzle(
        7,
        "Grotte, mauvais côté",
        "0.0 1800.0 300.0 1200.0 1000.0 1550.0 2000.0 1200.0 2500.0 1650.0 3700.0 220.0 4700.0 220.0 4750.0 1000.0 4700.0 1650.0 4000.0 1700.0 3700.0 1600.0 3750.0 1900.0 4000.0 2100.0 4900.0 2050.0 5100.0 1000.0 5500.0 500.0 6200.0 800.0 6999.0 600.0",
        State(x = 6500.0, y = 2000.0, xSpeed = 0.0, ySpeed = 0.0, fuel = 1200, rotate = 0, power = 0)
    )


)
val PUZZLE_MAP = PUZZLES.associateBy { it.id }


data class Segment(val start: Pair<Double, Double>, val end: Pair<Double, Double>)

data class Surface(
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

enum class CrossingEnum {
    NOPE, CRASH, LANDING_ZONE
}



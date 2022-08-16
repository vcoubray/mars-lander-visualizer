package condigame

import kotlin.math.*

fun toRadians(value: Double) = value * PI / 180
val Y_VECTOR = (-90..90).associateWith { cos(toRadians(it.toDouble())) }
val X_VECTOR = (-90..90).associateWith { -sin(toRadians(it.toDouble())) }



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


    fun cross(s2: Segment): Point? {
        val s1x = end.x - start.x
        val s1y = end.y - start.y
        val s2x = s2.end.x - s2.start.x
        val s2y = s2.end.y - s2.start.y

        if (s2x * s1y == s1x * s2y) return null

        val s =
            (-s1y * (start.x - s2.start.x) + s1x * (start.y - s2.start.y)) / (-s2x * s1y + s1x * s2y)
        val t =
            (-s2y * (start.x - s2.start.x) + s2x * (start.y - s2.start.y)) / (-s2x * s1y + s1x * s2y)

        if (s in 0.0..1.0 && t in 0.0..1.0) {
            return Point(
                start.x + (t * s1x),
                start.y + (t * s1y)
            )
        }
        return null
    }

}


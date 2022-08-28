package condigame

import Puzzle
import codingame.HEIGHT
import codingame.WIDTH

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

    fun cross(path: Segment): Pair<Segment, Point>? {
        for (segment in segments) {
            segment.cross(path)?.let {
                return segment to it
            }
        }
        return null
    }
}

fun Puzzle.toSurface() = Surface(
    HEIGHT, WIDTH,
    surface.split(" ")
        .asSequence()
        .map { it.toDouble() }
        .chunked(2)
        .map { (x, y) -> Point(x, y) }
        .windowed(2)
        .map { (a, b) -> Segment(a, b) }
        .toList()
)

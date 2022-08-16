package models

import condigame.*


data class Puzzle(
    val id: Int,
    val title: String,
    val surface: String,
    val initialState: State
) {
    val surfacePath = Surface(
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
}

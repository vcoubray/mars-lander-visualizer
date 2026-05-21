package fr.vco.genetic.algorithm.visualizer.marslanding


import fr.vco.genetic.algorithm.visualizer.Puzzle
import fr.vco.genetic.algorithm.visualizer.State
import fr.vco.genetic.algorithm.visualizer.codingame.HEIGHT
import fr.vco.genetic.algorithm.visualizer.codingame.WIDTH


fun MarsState.toState() = State(
    x = x,
    y = y,
    xSpeed = xSpeed,
    ySpeed = ySpeed,
    fuel = fuel,
    rotate = rotate,
    power = power,
)

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






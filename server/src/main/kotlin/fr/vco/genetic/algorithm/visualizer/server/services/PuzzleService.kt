package fr.vco.genetic.algorithm.visualizer.server.services

import fr.vco.genetic.algorithm.visualizer.marslanding.MARS_PUZZLES


class PuzzleService {

    val puzzles = MARS_PUZZLES

    fun getPuzzle(id: Int) = puzzles.getOrNull(id)

}

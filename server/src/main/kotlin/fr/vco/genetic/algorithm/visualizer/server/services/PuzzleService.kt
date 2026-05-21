package fr.vco.genetic.algorithm.visualizer.server.services

import fr.vco.genetic.algorithm.visualizer.puzzle.PuzzleRegistry

class PuzzleService(private val registry: PuzzleRegistry) {

    val puzzles get() = registry.allScenarios

    fun getPuzzle(id: Int) = puzzles.firstOrNull { it.id == id }
}

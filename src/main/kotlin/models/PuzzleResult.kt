package models

data class PuzzleResults(
    val puzzle: Puzzle,
    var results : List<PuzzleResult>
)

data class PuzzleResult(
    var id: Int,
    var generationCount: Int,
    var executionTime: Long
)
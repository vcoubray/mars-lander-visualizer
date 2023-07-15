package condigame



import MarsChromosomeResult
import Puzzle
import algorithm.Chromosome
import codingame.HEIGHT
import codingame.WIDTH
import java.lang.IllegalArgumentException


fun Chromosome.toResult(id:Int) =
    when(this) {
        is MarsChromosome -> toResult(id)
        else -> throw IllegalArgumentException("Unknown Chromosome type")
    }


fun MarsChromosome.toResult(id: Int) = MarsChromosomeResult(
    id,
    actions = actions.toList(),
    path = path,
    state = state,
    score = score,
    normalizedScore = normalizedScore,
    cumulativeScore = cumulativeScore,
    fitnessResult = fitnessResult
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






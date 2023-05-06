package drawers

import AlgoSettings
import Generation
import IndividualResult
import Puzzle
import codingame.CrossingEnum
import csstype.NamedColor
import org.w3c.dom.CanvasRenderingContext2D

class MarsGenerationDrawer(
    private val generation: Generation?,
    private val puzzle: Puzzle?,
    private val settings: AlgoSettings?,
    private val selectedIndividualId: Int?,
) : Drawer(7000, 3000) {

    override fun draw(context: CanvasRenderingContext2D) {
        context.init()
        puzzle?.let { context.drawSurface(it.surface) }
        generation?.let { context.drawPopulation(it.population, settings?.maxScore() ?: .0) }
    }

    private fun CanvasRenderingContext2D.init() {
        fillStyle = NamedColor.black
        fillRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())
    }

    private fun CanvasRenderingContext2D.drawSurface(surface: String) {
        val points = surface.split(" ")
            .chunked(2)
            .map { (x, y) -> x.toDouble() to y.toDouble() }
        drawPath(points, NamedColor.red)
    }

    private fun CanvasRenderingContext2D.drawPopulation(population: List<IndividualResult>, maxScore: Double) {
        population.sortedBy { it.score }.forEach { individual ->
            val color = when {
                individual.fitnessResult?.status == CrossingEnum.NOPE -> NamedColor.grey
                individual.fitnessResult?.status == CrossingEnum.CRASH -> NamedColor.orange
                individual.score < maxScore -> NamedColor.yellow
                else -> NamedColor.green
            }
            drawPath(individual.path, color)
        }

        // Redraw selected to have it in front of others
        population.firstOrNull { it.id == selectedIndividualId }
            ?.let { drawPath(it.path, NamedColor.red) }
    }

}
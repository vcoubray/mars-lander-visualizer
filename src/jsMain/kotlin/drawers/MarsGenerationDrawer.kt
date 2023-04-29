package drawers

import Generation
import Puzzle
import components.visualizer.ZOOM_FACTOR
import components.visualizer.drawPath
import csstype.NamedColor
import org.w3c.dom.CanvasRenderingContext2D

class MarsGenerationDrawer(
    private val generation: Generation?,
    private val puzzle: Puzzle?,
) : Drawer(7000, 3000) {

    override fun draw(context: CanvasRenderingContext2D) {
        context.init()

        puzzle?.let{
            context.drawSurface(it.surface)
        }

        generation?.let {
            generation.population.forEach {
                context.drawPath(it.path, NamedColor.red)
            }
        }
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

}
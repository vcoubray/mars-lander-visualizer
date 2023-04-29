package drawers

import Generation
import components.visualizer.ZOOM_FACTOR
import csstype.NamedColor
import org.w3c.dom.CanvasRenderingContext2D

class MarsGenerationDrawer(private val generation: Generation?) : Drawer(7000,3000) {

    override fun draw(context: CanvasRenderingContext2D) {
        context.init()
        generation?.let {
            generation.population.forEach{
                context.drawPath(it.path, NamedColor.red)
            }
        }
    }

    private fun CanvasRenderingContext2D.init() {
        fillStyle = NamedColor.black
        fillRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())
    }

}
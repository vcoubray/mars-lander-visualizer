package libs.drawers

import web.cssom.NamedColor
import org.w3c.dom.CanvasRenderingContext2D
import kotlin.math.min

abstract class Drawer(private val width: Int, private val height: Int) {

    private var ratio: Double = 1.0

    abstract fun draw(context: CanvasRenderingContext2D)

    fun initZoom(canvasWidth: Int, canvasHeight: Int) {
        val wRatio = canvasWidth / width.toDouble()
        val hRatio = canvasHeight / height.toDouble()
        ratio = min(wRatio, hRatio)
    }

    protected fun CanvasRenderingContext2D.drawText(
        x: Double,
        y: Double,
        message: String,
        color: NamedColor = NamedColor.red,
    ) {
        font = "15px Arial"
        fillStyle = color
        fillText(message, x, y)
    }

    protected fun CanvasRenderingContext2D.drawPath(path: List<Pair<Double, Double>>, color: NamedColor) {
        val zoomedPath = path.map { zoom(it, true) }

        strokeStyle = color
        beginPath()

        val (startX, startY) = zoomedPath.first()
        moveTo(startX, startY)

        for ((x, y) in zoomedPath.drop(1)) {
            lineTo(x, y)
            stroke()
        }
    }

    private fun CanvasRenderingContext2D.zoom(point: Pair<Double, Double>, inverseHeight: Boolean = true) =
        if (inverseHeight) point.first * ratio to canvas.height - (point.second * ratio)
        else point.first * ratio to point.second * ratio


}
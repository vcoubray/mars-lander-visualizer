package drawers

import csstype.NamedColor
import org.w3c.dom.CanvasRenderingContext2D

abstract class Drawer(val ratio: Double) {

    abstract fun draw(context: CanvasRenderingContext2D)


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
        strokeStyle = color
        beginPath()
        val (startX, startY) = path.first()
        moveTo(startX * ratio, canvas.height - (startY * ratio))

        for ((x, y) in path.drop(1)) {
            lineTo(x * ratio, canvas.height - (y * ratio))
            stroke()
        }
    }
}
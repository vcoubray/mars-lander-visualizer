import csstype.NamedColor
import kotlinx.browser.document
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement


val surface = "0.0 100.0 1000.0 500.0 1500.0 1500.0 3000.0 1000.0 4000.0 150.0 5500.0 150.0 6999.0 800.0"

fun main() {


    val canvas = document.createElement("canvas") as HTMLCanvasElement
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    context.canvas.width = 700
    context.canvas.height = 300
    document.body!!.appendChild(canvas)

    context.fillStyle = NamedColor.black
    context.fillRect(0.0, 0.0, 700.0, 300.0)

    context.fillStyle = NamedColor.red
    drawSurface(context, surface)

}

fun drawLine(context: CanvasRenderingContext2D, x1: Double, y1: Double, x2: Double, y2: Double) {
    context.strokeStyle = NamedColor.red
    context.beginPath();
    context.moveTo(x1,y1);
    context.lineTo(x2, y2);
    context.stroke();
}

fun drawSurface(context: CanvasRenderingContext2D, surface: String) {
    context.strokeStyle = NamedColor.red
    context.beginPath()
    val points = surface.split(" ")
        .map{it.toDouble()/10}
        .chunked(2)
    val (startX, startY) = points.first()
    context.moveTo(startX,300 - startY)

    for ((x,y) in points) {
        context.lineTo(x, 300 - y)
        context.stroke()
    }

}
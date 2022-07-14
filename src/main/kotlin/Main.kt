import csstype.NamedColor
import kotlinx.browser.document
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import react.create
import react.dom.client.createRoot


val surface = "0.0 100.0 1000.0 500.0 1500.0 1500.0 3000.0 1000.0 4000.0 150.0 5500.0 150.0 6999.0 800.0"

fun main() {
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    val context = createCanvas(canvas, 700, 300)
    context.init()

    val container = document.createElement("div")
    document.body!!.appendChild(container)
    val app = App.create {
        this.canvas = context
    }
    createRoot(container).render(app)
}


fun createCanvas(canvas: HTMLCanvasElement, width: Int, height: Int): CanvasRenderingContext2D {
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    context.canvas.width = width
    context.canvas.height = height
    document.body!!.appendChild(canvas)
    return context
}

fun CanvasRenderingContext2D.init() {
    fillStyle = NamedColor.black
    fillRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())

    fillStyle = NamedColor.red
    drawSurface(surface)
}

fun CanvasRenderingContext2D.drawLine(color: NamedColor, x1: Double, y1: Double, x2: Double, y2: Double) {
    strokeStyle = color
    beginPath()
    moveTo(x1, y1)
    lineTo(x2, y2)
    stroke()
}

fun CanvasRenderingContext2D.drawSurface(surface: String) {
    strokeStyle = NamedColor.red
    beginPath()
    val points = surface.split(" ")
        .map { it.toDouble() / 10 }
        .chunked(2)
    val (startX, startY) = points.first()
    moveTo(startX, 300 - startY)

    for ((x, y) in points) {
        lineTo(x, 300 - y)
        stroke()
    }

}



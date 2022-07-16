import csstype.NamedColor
import kotlinx.browser.document
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement



fun createCanvas(canvas: HTMLCanvasElement, width: Int, height: Int): CanvasRenderingContext2D {
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    context.canvas.width = width
    context.canvas.height = height
    document.body!!.appendChild(canvas)
    return context
}

fun CanvasRenderingContext2D.init(puzzle: Puzzle) {
    fillStyle = NamedColor.black
    fillRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())

    fillStyle = NamedColor.red
    drawSurface(puzzle.surface)

}

fun CanvasRenderingContext2D.drawLine(color: NamedColor, x1: Double, y1: Double, x2: Double, y2: Double) {
    strokeStyle = color
    beginPath()
    moveTo(x1, y1)
    lineTo(x2, y2)
    stroke()
}

fun CanvasRenderingContext2D.drawRandomLine() {
    val x1 = (0..this.canvas.width).random().toDouble()
    val x2 = (0..this.canvas.width).random().toDouble()
    val y2 = (0..this.canvas.height).random().toDouble()
    val y1 = (0..this.canvas.height).random().toDouble()
    val colors = listOf(
        NamedColor.red,
        NamedColor.green,
        NamedColor.darkgreen,
        NamedColor.blue,
        NamedColor.orange,
        NamedColor.yellow,
        NamedColor.violet
    )
    drawLine(colors.random(), x1, y1, x2, y2)
}

fun CanvasRenderingContext2D.drawSurface(surface: String) {
    val points = surface.split(" ")
        .map { it.toDouble() / 10 }
        .chunked(2)
        .map{(x,y) -> x to y}
    drawPath(points, NamedColor.red)
}

fun CanvasRenderingContext2D.drawPath(path: List<Pair<Double, Double>>, color: NamedColor) {
    strokeStyle = color
    beginPath()
    val (startX, startY) = path.first()
    moveTo(startX, 300 - startY)

    for ((x, y) in path.drop(1)) {
        lineTo(x, 300 - y)
        stroke()
    }
}
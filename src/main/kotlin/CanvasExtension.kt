import csstype.NamedColor
import org.w3c.dom.CanvasRenderingContext2D

const val ZOOM_FACTOR = 0.2

class AlgoResult(
    val surface: String,
    val population: Array<Chromosome>,
    val generation: Int
)

fun CanvasRenderingContext2D.drawAlgoResult(result: AlgoResult, selectedChromosome: Chromosome?) {
    init(result.surface)

    val best = result.population.takeIf { it.isNotEmpty() }?.map { it.score }?.maxOrNull() ?: 0.0
    val mean = result.population.takeIf { it.isNotEmpty() }?.map { it.score }?.average() ?: 0.0
    drawInformations(result.generation, best, mean)

    for (chromosome in result.population) {
        val color = when {
            chromosome == selectedChromosome -> NamedColor.red
            chromosome.score < 50.0 -> NamedColor.orange
            chromosome.score < 100 -> NamedColor.yellow
            else -> NamedColor.green
        }
        if (chromosome.path.isNotEmpty()) {
            drawPath(chromosome.path.map { (x, y) -> x * ZOOM_FACTOR to y * ZOOM_FACTOR }, color)
        }
    }
}


fun CanvasRenderingContext2D.init(surface: String) {
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

fun CanvasRenderingContext2D.drawText(x: Double, y: Double, message: String) {
    font = "15px Arial"
    fillStyle = NamedColor.red
    fillText(message, x, y)

}

fun CanvasRenderingContext2D.drawInformations(generation: Int, best: Double, mean: Double) {
    drawText(10.0, 20.0, "Generation : $generation")
    drawText(10.0, 40.0, "Best : ${best.asDynamic().toFixed(5)}")
    drawText(10.0, 60.0, "Mean : ${mean.asDynamic().toFixed(5)}")

}

fun CanvasRenderingContext2D.drawSurface(surface: String) {
    val points = surface.split(" ")
        .map { it.toDouble() * ZOOM_FACTOR}
        .chunked(2)
        .map { (x, y) -> x to y }
    drawPath(points, NamedColor.red)
}

fun CanvasRenderingContext2D.drawPath(path: List<Pair<Double, Double>>, color: NamedColor) {
    strokeStyle = color
    beginPath()
    val (startX, startY) = path.first()
    moveTo(startX, canvas.height - startY)

    for ((x, y) in path.drop(1)) {
        lineTo(x, canvas.height - y)
        stroke()
    }
}
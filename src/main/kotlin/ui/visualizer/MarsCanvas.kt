package ui.visualizer

import AlgoResult
import Chromosome
import CrossingEnum
import csstype.NamedColor
import kotlinx.browser.document
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import react.FC
import react.Props
import react.dom.html.ReactHTML.canvas

const val ZOOM_FACTOR = 0.2

external interface MarsCanvasProps : Props {
    var algoResult: AlgoResult?
    var selectedChromosome: Chromosome?
    var maxScore: Double
    var autoStop: Boolean
}

val MarsCanvas = FC<MarsCanvasProps> { props ->

    react.useEffect(props.algoResult, props.selectedChromosome) {
        val canvas = document.getElementById("mars-canvas")!! as HTMLCanvasElement
        val canvasContext = canvas.getContext("2d") as CanvasRenderingContext2D
        props.algoResult?.let { result ->
            canvasContext.drawAlgoResult(
                result,
                props.selectedChromosome,
                props.autoStop,
                props.maxScore
            )
        }
    }

    canvas {
        id = "mars-canvas"
        height = 3000 * ZOOM_FACTOR
        width = 7000 * ZOOM_FACTOR
    }
}

fun CanvasRenderingContext2D.drawAlgoResult(
    result: AlgoResult,
    selectedChromosome: Chromosome?,
    hideBadChromosomes: Boolean,
    maxScore: Double
) {
    init(result.puzzle.surface)
    drawInformations(result.generation, result.best, result.mean)
    drawPopulation(result, selectedChromosome, hideBadChromosomes, maxScore)
}


fun CanvasRenderingContext2D.init(surface: String) {
    fillStyle = NamedColor.black
    fillRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())

    fillStyle = NamedColor.red
    drawSurface(surface)
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
        .map { it.toDouble() * ZOOM_FACTOR }
        .chunked(2)
        .map { (x, y) -> x to y }
    drawPath(points, NamedColor.red)
}

fun CanvasRenderingContext2D.drawPopulation(
    result: AlgoResult,
    selectedChromosome: Chromosome?,
    hideBadChromosomes: Boolean,
    maxScore: Double
) {
    val listToDraw = if (hideBadChromosomes && result.best >= maxScore) {
        result.population.filter { it.score >= maxScore }
    } else {
        result.population.toList()
    }
    for (chromosome in listToDraw.sortedBy { it.score }) {
        val color = when {
            chromosome.result?.status == CrossingEnum.NOPE -> NamedColor.grey
            chromosome.result?.status == CrossingEnum.CRASH -> NamedColor.orange
            chromosome.score < maxScore -> NamedColor.yellow
            else -> NamedColor.green
        }
        if (chromosome.path.isNotEmpty()) {
            drawPath(chromosome.path.map { (x, y) -> x * ZOOM_FACTOR to y * ZOOM_FACTOR }, color)
        }
        // Redraw selected to have it in front of others
        selectedChromosome?.let {
            drawPath(it.path.map { (x, y) -> x * ZOOM_FACTOR to y * ZOOM_FACTOR }, NamedColor.red)
        }
    }
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
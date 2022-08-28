package components.visualizer

import PopulationResult
import Puzzle
import codingame.Chromosome
import codingame.CrossingEnum
import codingame.HEIGHT
import codingame.WIDTH
import csstype.NamedColor
import kotlinx.browser.document
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import react.FC
import react.Props
import react.dom.html.ReactHTML.canvas
import kotlin.math.min

const val ZOOM_FACTOR = 0.2

external interface MarsCanvasProps : Props {
    var puzzle: Puzzle?
    var populationResult: PopulationResult?
    var selectedChromosome: Chromosome?
    var maxScore: Double
    var autoStop: Boolean
}

val MarsCanvas = FC<MarsCanvasProps> { props ->

    react.useEffect(props.puzzle, props.populationResult, props.selectedChromosome) {
        val canvas = document.getElementById("mars-canvas")!! as HTMLCanvasElement
        val canvasContext = canvas.getContext("2d") as CanvasRenderingContext2D

        canvasContext.drawAlgoResult(
            props.puzzle,
            props.populationResult,
            props.selectedChromosome,
            props.autoStop,
            props.maxScore
        )

    }

    canvas {
        id = "mars-canvas"
        height = 3000 * ZOOM_FACTOR
        width = 7000 * ZOOM_FACTOR
    }
}

fun CanvasRenderingContext2D.drawAlgoResult(
    puzzle: Puzzle?,
    result: PopulationResult?,
    selectedChromosome: Chromosome?,
    showOnlyWinner: Boolean,
    maxScore: Double
) {
    val ratioX = canvas.width / WIDTH.toDouble()
    val ratioY = canvas.height / HEIGHT.toDouble()
    val ratio = min(ratioX, ratioY)
    init(puzzle, ratio)
    drawInformations(result)
    result?.let {
        drawPopulation(result, ratio, selectedChromosome, showOnlyWinner, maxScore)
    }
}


fun CanvasRenderingContext2D.init(puzzle: Puzzle?, ratio: Double) {
    fillStyle = NamedColor.black
    fillRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())


    puzzle?.let {
        fillStyle = NamedColor.white
        val startX = puzzle.initialState.x * ratio
        val startY = canvas.height - puzzle.initialState.y * ratio
        fillRect(startX, startY, 1.0, 1.0)

        fillStyle = NamedColor.red
        drawSurface(puzzle.surface, ratio)
    }
}

fun CanvasRenderingContext2D.drawText(x: Double, y: Double, message: String) {
    font = "15px Arial"
    fillStyle = NamedColor.red
    fillText(message, x, y)
}

fun CanvasRenderingContext2D.drawInformations(result: PopulationResult?) {
    drawText(10.0, 20.0, "Generation : ${result?.generation ?: 0}")
    drawText(10.0, 40.0, "Best : ${(result?.best ?: 0).asDynamic().toFixed(5)}")
    drawText(10.0, 60.0, "Mean : ${(result?.mean ?: 0).asDynamic().toFixed(5)}")
}

fun CanvasRenderingContext2D.drawSurface(surface: String, ratio: Double) {
    val points = surface.split(" ")
        .map { it.toDouble() * ratio }
        .chunked(2)
        .map { (x, y) -> x to y }
    drawPath(points, NamedColor.red)
}

fun CanvasRenderingContext2D.drawPopulation(
    result: PopulationResult,
    ratio: Double,
    selectedChromosome: Chromosome?,
    showOnlyWinner: Boolean,
    maxScore: Double
) {
    val listToDraw = if (showOnlyWinner && result.best >= maxScore) {
        result.population.filter { it.score >= maxScore }
    } else {
        result.population.toList()
    }
    for (chromosome in listToDraw.sortedBy { it.score }) {
        val color = when {
            chromosome.fitnessResult?.status == CrossingEnum.NOPE -> NamedColor.grey
            chromosome.fitnessResult?.status == CrossingEnum.CRASH -> NamedColor.orange
            chromosome.score < maxScore -> NamedColor.yellow
            else -> NamedColor.green
        }
        if (chromosome.path.isNotEmpty()) {
            drawPath(chromosome.path.map { (x, y) -> x * ratio to y * ratio }, color)
        }
        // Redraw selected to have it in front of others
        selectedChromosome?.let {
            drawPath(it.path.map { (x, y) -> x * ratio to y * ratio }, NamedColor.red)
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
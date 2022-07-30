package components

import AlgoResult
import Chromosome
import ZOOM_FACTOR
import drawAlgoResult
import kotlinx.browser.document
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import react.FC
import react.Props
import react.dom.html.ReactHTML.canvas

external interface MarsCanvasProps : Props {
    var algoResult: AlgoResult?
    var selectedChromosome: Chromosome?
}

val MarsCanvas = FC<MarsCanvasProps> { props ->

    react.useEffect(props.algoResult, props.selectedChromosome) {
        val canvas = document.getElementById("mars-canvas")!! as HTMLCanvasElement
        val canvasContext = canvas.getContext("2d") as CanvasRenderingContext2D
        props.algoResult?.let{result -> canvasContext.drawAlgoResult(result, props.selectedChromosome)}
    }

    canvas {
        id = "mars-canvas"
        height = 3000 * ZOOM_FACTOR
        width = 7000 * ZOOM_FACTOR
    }
}
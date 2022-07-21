package Components

import AlgoResult
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
}

val MarsCanvas = FC<MarsCanvasProps> { props ->

    react.useEffect(props.algoResult) {
        val canvas = document.getElementById("mars-canvas")!! as HTMLCanvasElement
        val canvasContext = canvas.getContext("2d") as CanvasRenderingContext2D
        props.algoResult?.let(canvasContext::drawAlgoResult)
    }

    canvas {
        id = "mars-canvas"
        height = 3000 * ZOOM_FACTOR
        width = 7000 * ZOOM_FACTOR
    }
}
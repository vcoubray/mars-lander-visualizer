package components.player

import components.visualizer.ZOOM_FACTOR
import drawers.Drawer
import kotlinx.browser.document
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import react.FC
import react.Props
import react.dom.html.ReactHTML.canvas

external interface PlayerCanvasProps : Props {
    var drawer: Drawer
}

val PlayerCanvas = FC<PlayerCanvasProps> { props ->

    react.useEffect(props.drawer) {
        val canvas = document.getElementById("player-canvas")!! as HTMLCanvasElement
        val canvasContext = canvas.getContext("2d") as CanvasRenderingContext2D
        props.drawer.draw(canvasContext)
    }

    canvas {
        id = "player-canvas"
        height = 3000 * ZOOM_FACTOR
        width = 7000 * ZOOM_FACTOR
    }


}
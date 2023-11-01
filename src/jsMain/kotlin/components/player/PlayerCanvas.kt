package components.player

import libs.drawers.Drawer
import kotlinx.browser.document
import kotlinx.browser.window
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
        val cs = window.getComputedStyle(canvas)
        canvas.height = cs.height.removeSuffix("px").toDouble().toInt()
        canvas.width = cs.width.removeSuffix("px").toDouble().toInt()

        props.drawer.initZoom(canvas.width, canvas.height)
        props.drawer.draw(canvasContext)
    }

    canvas {
        id = "player-canvas"
    }

}
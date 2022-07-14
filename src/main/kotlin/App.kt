import csstype.NamedColor
import org.w3c.dom.CanvasRenderingContext2D
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.input

external interface CanvasProps : Props {
    var canvas: CanvasRenderingContext2D
}

val App = FC<CanvasProps> { props ->

    input {
        type = InputType.button
        value = "Add a line"
        onClick = {
            val x1 = (0..props.canvas.canvas.width).random().toDouble()
            val x2 = (0..props.canvas.canvas.width).random().toDouble()
            val y1 = (0..props.canvas.canvas.height).random().toDouble()
            val y2 = (0..props.canvas.canvas.height).random().toDouble()
            val colors = listOf(NamedColor.red, NamedColor.green,NamedColor.darkgreen, NamedColor.blue,NamedColor.orange, NamedColor.yellow, NamedColor.violet)
            props.canvas.drawLine(colors.random(), x1, y1, x2, y2)
        }
    }

    input {
        type = InputType.button
        value = "Reset"
        onClick = {
            props.canvas.init()
        }
    }

}
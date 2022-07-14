import csstype.NamedColor
import org.w3c.dom.CanvasRenderingContext2D
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select

external interface CanvasProps : Props {
    var canvas: CanvasRenderingContext2D
    var puzzle: Puzzle
    var onPuzzleChange: (Puzzle) -> Unit
    var onReset: () -> Unit
}

val App = FC<CanvasProps> { props ->

    select {
        for (puzzle in puzzles) {
            option {
                value = puzzle.id.toString()
                label = puzzle.title
                selected = puzzle == props.puzzle
            }
        }
        onChange = {
            props.onPuzzleChange(puzzleMap[it.target.value.toInt()]!!)
        }
    }
    input {
        type = InputType.button
        value = "Add a line"
        onClick = {
            val x1 = (0..props.canvas.canvas.width).random().toDouble()
            val x2 = (0..props.canvas.canvas.width).random().toDouble()
            val y1 = (0..props.canvas.canvas.height).random().toDouble()
            val y2 = (0..props.canvas.canvas.height).random().toDouble()
            val colors = listOf(
                NamedColor.red,
                NamedColor.green,
                NamedColor.darkgreen,
                NamedColor.blue,
                NamedColor.orange,
                NamedColor.yellow,
                NamedColor.violet
            )
            props.canvas.drawLine(colors.random(), x1, y1, x2, y2)
        }
    }

    input {
        type = InputType.button
        value = "Reset"
        onClick = {
            props.onReset()
        }
    }

}
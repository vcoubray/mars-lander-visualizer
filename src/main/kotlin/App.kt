import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select

external interface CanvasProps : Props {
    var puzzle: Puzzle
    var onPuzzleChange: (Puzzle) -> Unit
    var onNext: ()-> Unit
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
        value = "Next Generation"
        onClick = {
            props.onNext()
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
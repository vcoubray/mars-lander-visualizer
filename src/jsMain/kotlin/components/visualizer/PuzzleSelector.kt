package components.visualizer

import Puzzle
import csstype.pct
import mui.material.MenuItem
import mui.material.Select
import mui.material.Size
import mui.system.sx
import react.FC
import react.Props


external interface PuzzleSelectorProps : Props {
    var puzzles: List<Puzzle>
    var defaultValue: Int
    var onPuzzleChange: (Int) -> Unit
}

val PuzzleSelector = FC<PuzzleSelectorProps> { props ->

    Select {

        sx {
            width = 100.pct
        }

        props.puzzles.forEachIndexed { i, puzzle ->
            MenuItem {
                value = i
                +puzzle.title
            }
        }

        size = Size.small
        value = props.defaultValue
        onChange = { event, _ -> props.onPuzzleChange(event.target.value.unsafeCast<Int>()) }
    }
}

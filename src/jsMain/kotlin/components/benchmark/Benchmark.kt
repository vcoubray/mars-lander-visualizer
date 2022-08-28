package components.benchmark

import apis.getPuzzles
import csstype.Display
import csstype.FlexWrap
import kotlinx.coroutines.*
import Puzzle
import PuzzleResult
import apis.algoPlay
import kotlinx.coroutines.channels.Channel
import mui.icons.material.PlayArrowSharp
import mui.material.Box
import mui.material.Button
import mui.material.ButtonVariant
import mui.system.sx
import react.*

val mainScope = MainScope()

val Benchmark = FC<Props> {

    var puzzles by useState(emptyList<Pair<Puzzle, Channel<PuzzleResult>>>())

    useEffectOnce {
        mainScope.launch {
            puzzles = getPuzzles().map { it to Channel() }
        }
    }


    Button {
        +"Play "
        startIcon = PlayArrowSharp.create()
        variant = ButtonVariant.contained

        onClick = {
            mainScope.launch {
                puzzles.forEach { (puzzle, channel) ->
                    val settings = Config.defaultSettings.copy(puzzleId = puzzle.id)
                    channel.send(algoPlay(settings))
                }
            }
        }
    }

    Box {
        sx {
            display = Display.flex
            flexWrap = FlexWrap.wrap
        }

        puzzles.forEach { (puzzle, channel) ->
            PuzzleStats {
                this.puzzle = puzzle
                this.channel = channel
            }
        }
    }
}
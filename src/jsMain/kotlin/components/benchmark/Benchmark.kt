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


data class Test(
    val counter: Int,
    val puzzleResult: PuzzleResult
)


val mainScope = MainScope()

val Benchmark = FC<Props> {

    var puzzles by useState(emptyList<Pair<Puzzle, Channel<PuzzleResult?>>>())
    val testCount = 10
    useEffectOnce {
        mainScope.launch {
            puzzles = getPuzzles().map { it to Channel(1) }
        }
    }


    Button {
        +"Play "
        startIcon = PlayArrowSharp.create()
        variant = ButtonVariant.contained

        onClick = {
            mainScope.launch {

                puzzles.forEach{(_,channel) -> channel.send(null)}
                puzzles.forEach { (puzzle, channel) ->
                    val settings = Config.defaultSettings.copy(puzzleId = puzzle.id)
                    repeat(testCount) {
                        channel.send(algoPlay(settings))
                    }
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
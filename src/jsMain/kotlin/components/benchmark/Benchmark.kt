package components.benchmark

import apis.getPuzzles
import csstype.Display
import csstype.FlexWrap
import kotlinx.coroutines.*
import Puzzle
import RunStats
import apis.algoPlay
import kotlinx.coroutines.channels.Channel
import mui.icons.material.PlayArrowSharp
import mui.material.*
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.onChange


val mainScope = MainScope()

val Benchmark = FC<Props> {

    var puzzles by useState(emptyList<Pair<Puzzle, Channel<List<RunStats>>>>())
    var runCount by useState(10)

    useEffectOnce {
        mainScope.launch {
            puzzles = getPuzzles().map { it to Channel(1) }
        }
    }

    fun startRuns() = mainScope.launch {

        puzzles.forEach { (puzzle, channel) ->
            var list = emptyList<RunStats>()
            channel.send(list)

            val settings = Config.defaultSettings.copy(puzzleId = puzzle.id)
            repeat(runCount) {
                list = list + algoPlay(settings)
                channel.send(list)
            }
        }
    }


    TextField {
        label = Typography.create { +"Runs count" }
        variant = FormControlVariant.outlined
        size = Size.small
        defaultValue = runCount

        onChange = { event ->
            runCount = event.target.unsafeCast<HTMLInputElement>().value.toInt()
        }
    }


    Button {
        +"Play "
        startIcon = PlayArrowSharp.create()
        variant = ButtonVariant.contained
        onClick = { startRuns() }
    }

    Box {
        sx {
            display = Display.flex
            flexWrap = FlexWrap.wrap
        }

        puzzles.forEach { (puzzle, channel) ->
            RunStats {
                this.puzzle = puzzle
                this.channel = channel
                this.runCount = runCount
            }
        }
    }
}
package components.benchmark

import Config
import Puzzle
import RunStats
import apis.algoPlay
import apis.getPuzzles
import components.common.AlgoSettings
import csstype.Display
import csstype.FlexWrap
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import mui.icons.material.PlayArrowSharp
import mui.icons.material.StopSharp
import mui.material.*
import mui.system.responsive
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.onChange


val mainScope = MainScope()

val Benchmark = FC<Props> {

    var puzzles by useState(emptyList<Pair<Puzzle, Channel<List<RunStats>>>>())
    var runCount by useState(20)
    var algoSettings by useState(Config.defaultSettings.copy())
    var timeout by useState(250)

    var runsJob: Job? by useState(null)
    var isPlaying by useState(runsJob?.isActive == true)


    useEffectOnce {
        mainScope.launch {
            puzzles = getPuzzles().map { it to Channel(1) }
        }
    }

    fun startRuns() {
        runsJob = mainScope.launch {
            isPlaying = true
            puzzles.forEach { (puzzle, channel) ->
                var runs = emptyList<RunStats>()
                channel.send(runs)

                val settings = algoSettings.copy(puzzleId = puzzle.id)
                repeat(runCount) {
                    runs = runs + algoPlay(settings)
                    channel.send(runs)
                }
            }
            isPlaying = false
        }
    }

    fun stop() {
        runsJob?.cancel()
        isPlaying = false
    }

    Box {
        sx {
            display = Display.flex
        }


        Stack {
            spacing = responsive(2)

            AlgoSettings {
                this.algoSettings = algoSettings
                this.onUpdateSettings = { settings -> algoSettings = settings }
            }

            Divider()

            TextField {
                label = Typography.create { +"Runs count" }
                variant = FormControlVariant.outlined
                size = Size.small
                defaultValue = runCount

                onChange = { event ->
                    runCount = event.target.unsafeCast<HTMLInputElement>().value.toInt()
                }
            }

            TextField {
                label = Typography.create { +"Timeout (ms)" }
                variant = FormControlVariant.outlined
                size = Size.small
                defaultValue = timeout

                onChange = { event ->
                    timeout = event.target.unsafeCast<HTMLInputElement>().value.toInt()
                }
            }

            if (isPlaying) {
                Button {
                    +"Stop "
                    startIcon = StopSharp.create()
                    variant = ButtonVariant.contained
                    onClick = { stop() }
                }

            } else {
                Button {
                    +"Play "
                    startIcon = PlayArrowSharp.create()
                    variant = ButtonVariant.contained
                    onClick = { startRuns() }
                }
            }
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
                    this.timeout = timeout
                }
            }
        }
    }
}
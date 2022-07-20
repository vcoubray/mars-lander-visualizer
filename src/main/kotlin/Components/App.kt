package Components

import AlgoSettings
import Puzzle
import csstype.px
import emotion.react.css
import kotlinx.js.timers.Timeout
import kotlinx.js.timers.clearInterval
import kotlinx.js.timers.setInterval
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.useState

external interface CanvasProps : Props {
    var puzzles: List<Puzzle>
    var algoSettings: AlgoSettings
    var onNext: () -> Unit
    var onUpdateSettings: (AlgoSettings) -> Unit
}

val App = FC<CanvasProps> { props ->

    var intervalId: Timeout? by useState(null)

    fun stop() {
        intervalId?.let {
            clearInterval(it)
            intervalId = null
        }

    }

    div {
        css {
            marginLeft = 10.px
        }

        AlgoSettings {
            this.puzzles = props.puzzles
            this.algoSettings = props.algoSettings
            this.onUpdateSettings = { algoSettings ->
                stop()
                props.onUpdateSettings(algoSettings)
            }
        }

        MediaControls {
            this.intervalId = intervalId
            this.onNext = {
                stop()
                props.onNext()
            }
            this.onPlay = {
                intervalId = setInterval(props.onNext, 100)
            }
            this.onStop = {
                stop()
            }
        }
    }
}
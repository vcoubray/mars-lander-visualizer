package components.player

import csstype.ClassName
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import mainScope
import mui.icons.material.ChevronLeft
import mui.icons.material.ChevronRight
import mui.icons.material.PlayArrow
import mui.icons.material.Stop
import react.*
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.span

external interface PlayerControlsProps : Props {
    var max: Int
    var defaultValue: Int
    var onChange: (Int) -> Unit
}

val PlayerControls = FC<PlayerControlsProps> { props ->

    var value by useState(0)
    val playJobRef = useRef<Job>(null)
    var isPlaying by useState(false)

    fun start() {
        playJobRef.current = mainScope.launch {
            isPlaying = true
            var selectedValue = value
            while (this.isActive && selectedValue < props.max) {
                value = ++selectedValue
                delay(100)
            }
        }
    }

    fun stop() {
        playJobRef.current?.cancel()
        playJobRef.current = null
        isPlaying = false
    }

    // Stop Player ( if running ) when unmount the component
    useEffectOnce {
        this.cleanup { stop() }
    }

    useEffect(value) {
        props.onChange(value)
    }

    div {
        className = ClassName("player-controller")

        ProgressReaderBar {
            this.max = props.max
            this.value = value
            this.onChange = { it ->
                stop()
                value = it
            }
        }

        div {
            className = ClassName("grid")
            button {
                ChevronLeft()
                onClick = { _ ->
                    if (value > 0) {
                        stop()
                        value -= 1
                    }
                }
            }
            if (!isPlaying) {
                button {
                    PlayArrow()
                    onClick = { start() }
                }
            } else {
                button {
                    Stop()
                    onClick = { stop() }
                }
            }
            button {
                ChevronRight()
                onClick = { _ ->
                    if (value < props.max) {
                        stop()
                        value += 1
                    }
                }
            }
            span {
                +"$value / ${props.max}"
            }
        }
    }

}

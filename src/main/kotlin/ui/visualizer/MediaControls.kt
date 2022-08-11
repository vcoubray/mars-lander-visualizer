package ui.visualizer

import kotlinx.js.timers.Timeout
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label

external interface MediaControlsProps : Props {
    var intervalId: Timeout?
    var autoStop: Boolean
    var refreshRate: Int
    var onPlay: () -> Unit
    var onStop: () -> Unit
    var onNext: () -> Unit
    var onReset: () -> Unit
    var toggleAutoStop: (Boolean) -> Unit
    var onUpdateRefreshRate: (Int) -> Unit
}


val MediaControls = FC<MediaControlsProps> { props ->

    input {
        type = InputType.button
        value = "Next"
        disabled = props.intervalId != null
        onClick = {
            props.onNext()
        }
    }

    if (props.intervalId == null) {
        input {
            type = InputType.button
            value = "Play"
            onClick = {
                props.onPlay()
            }
        }
    } else {
        input {
            type = InputType.button
            value = "Stop"
            onClick = {
                props.onStop()
            }
        }
    }
    input {
        type = InputType.button
        value = "Reset"
        onClick = {
            props.onReset()
        }
    }

    label {
        +"Auto Stop"
        input {
            type = InputType.checkbox
            checked = props.autoStop
            onClick = {
                props.toggleAutoStop(!props.autoStop)
            }
        }
    }
    label {
        +"Refresh Rate"
        input {
            type = InputType.text
            defaultValue = props.refreshRate.toString()
            onChange = { event ->
                props.onUpdateRefreshRate(event.target.value.toInt())
            }
        }
    }
}
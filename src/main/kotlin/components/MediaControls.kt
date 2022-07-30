package components

import kotlinx.js.timers.Timeout
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.input

external interface MediaControlsProps : Props {
    var intervalId: Timeout?
    var onPlay: () -> Unit
    var onStop: () -> Unit
    var onNext: () -> Unit
    var onReset: () -> Unit
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
}
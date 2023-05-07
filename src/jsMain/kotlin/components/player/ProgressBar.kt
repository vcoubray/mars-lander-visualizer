package components.player

import web.cssom.ClassName
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import web.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.progress

external interface PlayerProgressBarProps : Props {
    var max: Int
    var value: Int
    var onChange: (Int) -> Unit
}

val PlayerProgressBar = FC<PlayerProgressBarProps> { props ->

    div {
        className = ClassName("player-progress-bar")

        progress {
            max = props.max.toDouble()
            value = props.value
        }

        input {
            type = InputType.range
            min = 0
            max = props.max
            value = props.value
            onChange = { event -> props.onChange(event.target.unsafeCast<HTMLInputElement>().value.toInt()) }
        }
    }
}
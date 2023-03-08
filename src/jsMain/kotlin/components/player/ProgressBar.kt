package components.player

import csstype.ClassName
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.progress

external interface ProgressReaderBarProps : Props {
    var max: Int
    var value: Int
    var onChange: (Int) -> Unit
}

val progressReaderBar = FC<ProgressReaderBarProps> { props ->

    div {
        className = ClassName("progress-reader-bar")

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
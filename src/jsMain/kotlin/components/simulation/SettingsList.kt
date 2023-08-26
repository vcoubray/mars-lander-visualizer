package components.simulation

import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.ul
import web.cssom.ClassName

external interface ReadOnlyFormProps : Props {
    var settings: Map<String, String>
}

val ReadOnlyForm = FC<ReadOnlyFormProps> { props ->

    ul {
        className = ClassName("read-only-form")

        props.settings.forEach { (label, value) ->
            li {
                span { +label }
                span { +value }
            }
        }
    }

}
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML

data class FormField(
    val label: String,
    val type: InputType,
    var value: String,
)

external interface FormProps : Props {
    var fields: List<FormField>
    var onChange: (List<FormField>) -> Unit
}


val Form = FC<FormProps> { props ->

    for (field in props.fields) {
        ReactHTML.label {
            +field.label
            ReactHTML.input {
                type = field.type
                defaultValue = field.value
                onChange = { event ->
                    field.value = event.target.unsafeCast<HTMLInputElement>().value
                    props.onChange(props.fields)
                }
            }
        }
    }
}

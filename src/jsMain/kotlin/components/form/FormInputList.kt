package components.form

import components.form.models.FormField
import react.FC
import react.Props
import react.dom.html.ReactHTML

external interface FormInputFieldListProps : Props{
    var fields: List<FormField>
    var values: MutableMap<String, String>
}


var FormInputList = FC <FormInputFieldListProps> { props ->
    for (field in props.fields) {
        ReactHTML.label {
            +field.label
            ReactHTML.input {
                type = field.type
                defaultValue = props.values[field.id]
                onChange = { event ->
                    props.values[field.id] = event.target.value
                }
            }

        }
    }
}
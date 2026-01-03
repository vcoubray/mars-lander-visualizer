package components.form.models

import web.html.InputType

data class FormField(
    val id: String,
    val label: String,
    val type: InputType,
)



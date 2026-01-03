package components.form.models

import react.Props

external interface SettingsFormProps : Props {
    var settingsValues: MutableMap<String, String>
}
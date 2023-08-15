package components.form

import MarsEngineSettings
import SimulationSettings
import components.form.mappers.toGlobalSettings
import components.form.mappers.toMapValues
import components.form.mappers.toMarsEngineSettings
import react.FC
import react.Props


class MarsSimulationFormControl(
    simulationSettings: SimulationSettings<MarsEngineSettings>,
) {
    val globalFormGroupControl = simulationSettings.globalSettings.toMapValues()
    val engineFormGroupControl = simulationSettings.engineSettings.toMapValues()

    fun getSettings() = SimulationSettings(
        globalSettings = globalFormGroupControl.toGlobalSettings(),
        engineSettings = engineFormGroupControl.toMarsEngineSettings()
    )
}

external interface MarsSimulationFormProps : Props {
    var formGroupControl: MarsSimulationFormControl
}

val MarsSimulationForm = FC<MarsSimulationFormProps> { props ->

    GlobalSettingsForm{
        settingsValues = props.formGroupControl.globalFormGroupControl
    }
    MarsEngineSettingsForm {
        settingsValues = props.formGroupControl.engineFormGroupControl
    }
}
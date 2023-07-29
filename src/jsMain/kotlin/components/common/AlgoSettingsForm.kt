package components.common


import Form
import FormField
import LimitType
import MarsEngineSettings
import Puzzle
import SimulationSettings
import apis.fetchPuzzles
import kotlinx.coroutines.launch
import mainScope
import react.FC
import react.Props
import web.html.InputType
import react.dom.html.ReactHTML.details
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.dom.html.ReactHTML.summary
import react.useEffectOnce
import react.useState

external interface AlgoSettingsProps : Props {
    var simulationsSettings: SimulationSettings<MarsEngineSettings>
    var onUpdateSettings: (SimulationSettings<MarsEngineSettings>) -> Unit
}


val AlgoSettingsForm = FC<AlgoSettingsProps> { props ->

    var puzzles by useState(emptyList<Puzzle>())


    useEffectOnce {
        mainScope.launch {
            puzzles = fetchPuzzles()
        }
    }

    details {
        summary {
            +"Genetic settings"
        }
        label {
            +"Limit type"
            select {
                LimitType.entries.forEach {
                    option {
                        label = it.label
                        value = it
                    }
                }
                onChange = { event ->
                    props.simulationsSettings.limitType = LimitType.valueOf(event.target.value)
                    props.onUpdateSettings(props.simulationsSettings)
                }
            }
        }
        Form {
            fields = listOf(
                FormField("Limit value", InputType.number, props.simulationsSettings.limitValue.toString())
                { value -> props.simulationsSettings.limitValue = value.toInt() },
                FormField("Population Size", InputType.number, props.simulationsSettings.populationSize.toString())
                { value -> props.simulationsSettings.populationSize = value.toInt() },
                FormField("Chromosome Size", InputType.number, props.simulationsSettings.chromosomeSize.toString())
                { value -> props.simulationsSettings.chromosomeSize = value.toInt() },
                FormField(
                    "Mutation probability (0 to 1)",
                    InputType.number,
                    props.simulationsSettings.mutationProbability.toString()
                ){ value -> props.simulationsSettings.mutationProbability = value.toDouble() },
                FormField("Elitism (0 to 1)", InputType.number, props.simulationsSettings.elitismPercent.toString())
                { value -> props.simulationsSettings.elitismPercent = value.toDouble() },
            )
            onChange = { props.onUpdateSettings(props.simulationsSettings) }
        }
    }

    MarsSettingsForm {
        marsSettings = props.simulationsSettings.engineSettings
        onUpdateSettings = { props.onUpdateSettings(props.simulationsSettings) }
    }

}
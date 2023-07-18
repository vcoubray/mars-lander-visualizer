package components.common


import Form
import FormField
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
        Form {
            fields = listOf(
                FormField("Population Size", InputType.number, props.simulationsSettings.populationSize.toString()),
                FormField("Chromosome Size", InputType.number, props.simulationsSettings.chromosomeSize.toString()),
                FormField(
                    "Mutation probability (0 to 1)",
                    InputType.number,
                    props.simulationsSettings.mutationProbability.toString()
                ),
                FormField("Elitism (0 to 1)", InputType.number, props.simulationsSettings.elitismPercent.toString())
            )
            onChange = { props.onUpdateSettings(props.simulationsSettings) }
        }
    }

    MarsSettingsForm {
        marsSettings = props.simulationsSettings.engineSettings
        onUpdateSettings = { props.onUpdateSettings(props.simulationsSettings) }
    }

}
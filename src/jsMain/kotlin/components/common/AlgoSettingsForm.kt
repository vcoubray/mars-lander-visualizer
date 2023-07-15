package components.common

import AlgoSettings
import Form
import FormField
import MarsSettings
import Puzzle
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
    var algoSettings: AlgoSettings
    var onUpdateSettings: (AlgoSettings) -> Unit
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
                FormField("Population Size", InputType.number, props.algoSettings.populationSize.toString()),
                FormField("Chromosome Size", InputType.number, props.algoSettings.chromosomeSize.toString()),
                FormField(
                    "Mutation probability (0 to 1)",
                    InputType.number,
                    props.algoSettings.mutationProbability.toString()
                ),
                FormField("Elitism (0 to 1)", InputType.number, props.algoSettings.elitismPercent.toString())
            )
            onChange = { props.onUpdateSettings(props.algoSettings) }
        }
    }

    MarsSettingsForm {
        marsSettings = props.algoSettings as MarsSettings
        onUpdateSettings = { props.onUpdateSettings(props.algoSettings) }
    }

}
package components.common

import AlgoSettings
import Form
import FormField
import Puzzle
import apis.fetchPuzzles
import kotlinx.coroutines.launch
import mainScope
import react.FC
import react.Props
import web.html.InputType
import react.dom.html.ReactHTML.details
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
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
            onChange = { println(fields) }
        }
    }
    details {
        summary {
            +"Mars Landing"
        }

        select {
            defaultValue = props.algoSettings.puzzleId
            puzzles.forEach { puzzle ->
                option{
                    value = puzzle.id
                    label = puzzle.title
                }
            }
            onChange = { event ->
                props.algoSettings.puzzleId = event.target.value.toInt()
            }
        }

        Form {
            fields = listOf(
                FormField("Speed Max", InputType.number, props.algoSettings.populationSize.toString()),
                FormField("X Speed weight", InputType.number, props.algoSettings.chromosomeSize.toString()),
                FormField("Y Speed weight", InputType.number, props.algoSettings.mutationProbability.toString()),
                FormField("Rotate weight", InputType.number, props.algoSettings.rotateWeight.toString()),
                FormField("Distance weight", InputType.number, props.algoSettings.distanceWeight.toString()),
                FormField("Speed weight (Crashing)", InputType.number, props.algoSettings.crashSpeedWeight.toString()),
            )
            onChange = { println(fields) }
        }
    }
}
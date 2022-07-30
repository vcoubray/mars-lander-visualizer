package components


import AlgoSettings
import PUZZLE_MAP
import Puzzle
import csstype.Display
import csstype.FlexDirection
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select


external interface AlgoSettingsProps: Props {
    var puzzles : List<Puzzle>
    var algoSettings: AlgoSettings
    var onUpdateSettings: (AlgoSettings) -> Unit
}


val AlgoSettings = FC<AlgoSettingsProps> {props ->

    select {
        for (puzzle in props.puzzles) {
            option {
                value = puzzle.id.toString()
                label = puzzle.title

            }
        }
        defaultValue = props.algoSettings.puzzle.id.toString()
        onChange = {
            props.algoSettings.puzzle = PUZZLE_MAP[it.target.value.toInt()]!!
            props.onUpdateSettings(props.algoSettings)
        }
    }

    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
        }
        label {
            +"Population Size"
            input {
                type = InputType.text
                defaultValue = props.algoSettings.populationSize.toString()
                onChange = { event ->
                    props.algoSettings.populationSize = event.target.value.toInt()
                    props.onUpdateSettings(props.algoSettings)
                }
            }
        }

        label {
            +"Chromosome Size"
            input {
                type = InputType.text
                defaultValue = props.algoSettings.chromosomeSize.toString()
                onChange = { event ->
                    props.algoSettings.chromosomeSize = event.target.value.toInt()
                    props.onUpdateSettings(props.algoSettings)
                }
            }
        }

        label {
            +"Mutation probability (0 to 1)"
            input {
                type = InputType.text
                defaultValue = props.algoSettings.mutationProbability.toString()
                onChange = { event ->
                    props.algoSettings.mutationProbability = event.target.value.toDouble()
                    props.onUpdateSettings(props.algoSettings)
                }
            }
        }

        label {
            +"Elitism (0 to 1)"
            input {
                type = InputType.text
                defaultValue = props.algoSettings.elitismPercent.toString()
                onChange = { event ->
                    props.algoSettings.elitismPercent = event.target.value.toDouble()
                    props.onUpdateSettings(props.algoSettings)
                }
            }
        }

        label {
            +"Speed max"
            input {
                type = InputType.text
                defaultValue = props.algoSettings.speedMax.toString()
                onChange = { event ->
                    props.algoSettings.speedMax = event.target.value.toDouble()
                    props.onUpdateSettings(props.algoSettings)
                }
            }
        }

        label {
            +"Speed weight"
            input {
                type = InputType.text
                defaultValue = props.algoSettings.speedWeight.toString()
                onChange = { event ->
                    props.algoSettings.speedWeight = event.target.value.toDouble()
                    props.onUpdateSettings(props.algoSettings)
                }
            }
        }

    }
}


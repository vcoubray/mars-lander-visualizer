package components.visualizer

import models.AlgoSettings
import PUZZLE_MAP
import models.Puzzle
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


val AlgoSettings = FC<AlgoSettingsProps> { props ->

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
            +"condigame.Chromosome Size"
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
            +"X Speed weight"
            input {
                type = InputType.text
                defaultValue = props.algoSettings.xSpeedWeight.toString()
                onChange = { event ->
                    props.algoSettings.xSpeedWeight = event.target.value.toDouble()
                    props.onUpdateSettings(props.algoSettings)
                }
            }
        }

        label {
            +"Y Speed weight"
            input {
                type = InputType.text
                defaultValue = props.algoSettings.ySpeedWeight.toString()
                onChange = { event ->
                    props.algoSettings.ySpeedWeight = event.target.value.toDouble()
                    props.onUpdateSettings(props.algoSettings)
                }
            }
        }

        label {
            +"Rotate weight"
            input {
                type = InputType.text
                defaultValue = props.algoSettings.rotateWeight.toString()
                onChange = { event ->
                    props.algoSettings.rotateWeight = event.target.value.toDouble()
                    props.onUpdateSettings(props.algoSettings)
                }
            }
        }

        label {
            +"Distance weight"
            input {
                type = InputType.text
                defaultValue = props.algoSettings.distanceWeight.toString()
                onChange = { event ->
                    props.algoSettings.distanceWeight = event.target.value.toDouble()
                    props.onUpdateSettings(props.algoSettings)
                }
            }
        }

        label {
            +"Speed weight (No landing zone)"
            input {
                type = InputType.text
                defaultValue = props.algoSettings.crashSpeedWeight.toString()
                onChange = { event ->
                    props.algoSettings.crashSpeedWeight = event.target.value.toDouble()
                    props.onUpdateSettings(props.algoSettings)
                }
            }
        }
    }
}


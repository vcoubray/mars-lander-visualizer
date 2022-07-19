import csstype.Display
import csstype.FlexDirection
import csstype.px
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select

external interface CanvasProps : Props {
    var algoSettings: AlgoSettings
    var onNext: () -> Unit
    var onUpdateSettings: (AlgoSettings) -> Unit
    var onPlay: () -> Unit
    var onStop: () -> Unit
}

val App = FC<CanvasProps> { props ->
    div {
        css {
            marginLeft = 10.px
        }
        select {
            for (puzzle in puzzles) {
                option {
                    value = puzzle.id.toString()
                    label = puzzle.title

                }
            }
            defaultValue = props.algoSettings.puzzle.id.toString()
            onChange = {
                props.algoSettings.puzzle = puzzleMap[it.target.value.toInt()]!!
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

        }

        input {
            type = InputType.button
            value = "Update Settings"
            onClick = {
                props.onUpdateSettings(props.algoSettings)
            }
        }


        input {
            type = InputType.button
            value = "Next Generation"
            onClick = {
                props.onNext()
            }
        }

        input {
            type = InputType.button
            value = "Play"
            onClick = {
                props.onPlay()
            }
        }
        input {
            type = InputType.button
            value = "Stop"
            onClick = {
                props.onStop()
            }
        }
    }
}
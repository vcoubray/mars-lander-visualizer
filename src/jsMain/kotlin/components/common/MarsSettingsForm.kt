package components.common

import Form
import FormField
import MarsEngineSettings
import MarsSettings
import Puzzle
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.details
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.dom.html.ReactHTML.summary
import react.useState
import web.html.InputType

external interface MarsSettingsProps: Props {
    var marsSettings: MarsEngineSettings
    var onUpdateSettings: (MarsSettings) -> Unit
}

val MarsSettingsForm = FC<MarsSettingsProps> { props ->

    val puzzles by useState(emptyList<Puzzle>())


    details {
        summary {
            +"Mars Landing"
        }

        select {
            defaultValue = props.marsSettings.puzzleId
            puzzles.forEach { puzzle ->
                option {
                    value = puzzle.id
                    label = puzzle.title
                }
            }
            onChange = { event ->
                props.marsSettings.puzzleId = event.target.value.toInt()
            }
        }

        Form {
            fields = listOf(
                FormField("Speed Max", InputType.number, props.marsSettings.speedMax.toString()),
                FormField("X Speed weight", InputType.number, props.marsSettings.xSpeedWeight.toString()),
                FormField("Y Speed weight", InputType.number, props.marsSettings.ySpeedWeight.toString()),
                FormField("Rotate weight", InputType.number, props.marsSettings.rotateWeight.toString()),
                FormField("Distance weight", InputType.number, props.marsSettings.distanceWeight.toString()),
                FormField("Speed weight (Crashing)", InputType.number, props.marsSettings.crashSpeedWeight.toString()),
            )
            onChange = { println(fields) }
        }
    }
}
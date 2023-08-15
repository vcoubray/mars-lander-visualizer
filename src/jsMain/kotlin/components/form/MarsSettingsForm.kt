package components.form

import Puzzle
import apis.fetchPuzzles
import components.form.models.FormField
import components.form.models.SettingsFormProps
import kotlinx.coroutines.launch
import mainScope
import react.FC
import react.dom.html.ReactHTML.details
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.dom.html.ReactHTML.summary
import react.useEffectOnce
import react.useState
import web.html.InputType


val MarsEngineSettingsForm = FC<SettingsFormProps> { props ->

    var puzzles by useState(emptyList<Puzzle>())

    useEffectOnce {
        mainScope.launch {
            puzzles = fetchPuzzles()
        }
    }

    details {
        summary {
            +"Mars Landing"
        }
        label {
            +"Puzzle"
            select {
                defaultValue = props.settingsValues["puzzleId"]
                puzzles.forEach { puzzle ->
                    option {
                        value = puzzle.id
                        label = puzzle.title
                    }
                }
                onChange = { event ->
                    props.settingsValues["puzzleId"] = event.target.value
                }
            }
        }
        FormInputList {
            fields = listOf(
                FormField("speedMax", "Speed Max", InputType.number),
                FormField("xSpeedWeight", "X Speed weight", InputType.number),
                FormField("ySpeedWeight", "Y Speed weight", InputType.number),
                FormField("rotateWeight", "Rotate weight", InputType.number),
                FormField("distanceWeight", "Distance weight", InputType.number),
                FormField("crashSpeedWeight", "Speed weight (Crashing)", InputType.number)
            )
            values = props.settingsValues
        }

    }
}
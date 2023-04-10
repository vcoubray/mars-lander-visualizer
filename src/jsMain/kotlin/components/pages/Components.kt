package components.pages

import Config
import SimulationStatus
import SimulationSummary
import components.common.AlgoSettingsForm
import components.common.SimulationSummaryComponent
import components.player.progressReaderBar
import csstype.ClassName
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.progress
import react.useState

val Components = FC<Props> { _ ->
    val algoSettings by useState(Config.defaultSettings.copy())
    var progressValue by useState(0)

    +"Components"

    button {
        +"play"
    }

    progress {
        max = 100.0
        value = 5
    }

    input {
        type = InputType.range
        min = 0
        max = 100
    }

    progressReaderBar {
        max = 251
        value = progressValue
        onChange = { value -> progressValue = value }
    }

    div {
        className = ClassName("grid")
        button {
            +"<"
            onClick = { progressValue -= 1 }
        }

        button {
            +">"
            onClick = { progressValue += 1 }
        }
    }


    AlgoSettingsForm {
        this.algoSettings = algoSettings
    }

    SimulationSummaryComponent{
        summary = SimulationSummary(0,SimulationStatus.COMPLETE, 320, 100.0, 150 )
    }
}

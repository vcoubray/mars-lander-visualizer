package components


import Config
import components.common.AlgoSettingsForm
import components.player.progressReaderBar
import csstype.ClassName
import mui.icons.material.Settings
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.details
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.main
import react.dom.html.ReactHTML.nav
import react.dom.html.ReactHTML.progress
import react.dom.html.ReactHTML.summary
import react.dom.html.ReactHTML.ul
import react.useState
import theme.*


val AppPicoCss = FC<Props> {
    nav {
        +"Mars Lander Visualizer"
    }
    main {
        className = ClassName("container-fluid")
        Components()
    }
}


val Components = FC<Props> { _ ->
    var algoSettings by useState(Config.defaultSettings.copy())
    var progressValue by useState(0)

    +"Hello world"

    button {
        +"play"
    }

    progress {
        max = 100.0
        value = 5
    }

    button {
        +"Dark Mode"
        onClick = { ThemeService.toggleMode() }
    }

    div {
        className = ClassName("grid")

        ThemeColor.values().forEach { color ->
            button {
                +color.value
                onClick = { ThemeService.changeColor(color) }
            }
        }
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


}

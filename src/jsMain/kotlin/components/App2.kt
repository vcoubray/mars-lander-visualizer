package components


import Theme.ThemeColor
import Theme.changeThemeColor
import Theme.toggleDarkMode
import csstype.ClassName
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.progress


val AppPicoCss = FC<Props> {
    Components()
}
val Components = FC<Props> { _ ->

    ReactHTML.main {

        className = ClassName("container-fluid")
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
            onClick = { toggleDarkMode() }
        }

        div {
            className = ClassName("grid")

            ThemeColor.values().forEach {color ->
                button{
                    +color.value
                    onClick= { changeThemeColor(color) }
                }
            }
        }

        input {
            type = InputType.range
            min = 0
            max = 100

        }

    }

}
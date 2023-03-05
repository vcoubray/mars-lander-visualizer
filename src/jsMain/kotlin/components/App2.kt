package components


import csstype.ClassName
import modules.*
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.progress
import react.useContext


val AppPicoCss = FC<Props> {
    PicoThemeModule {
        Components()
    }
}
val Components = FC<Props> { _ ->
    var themeContext by useContext(PicoThemeContext)

    div {

        className = ClassName("container")
        +"Hello"

        button {
            +"play"
        }

        progress {
            max = 100.0
            value = 5
        }

        button {
            +"Change theme"
            onClick = {
                themeContext = if (themeContext == PicoTheme.LIGHT) {
                    PicoTheme.DARK
                } else {
                    PicoTheme.LIGHT
                }

            }
        }

    }

}
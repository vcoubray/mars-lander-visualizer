package ui

import kotlinx.js.jso
import mui.material.CssBaseline
import mui.material.PaletteMode
import mui.material.styles.Theme
import mui.material.styles.ThemeProvider
import mui.material.styles.createTheme
import react.*

object Themes {
    val Light = createTheme(
        jso {
            palette = jso { mode = PaletteMode.light }
        }
    )

    val Dark = createTheme(
        jso {
            palette = jso { mode = PaletteMode.dark }
        }

    )
}

typealias ThemeState = StateInstance<Theme>

val ThemeContext = createContext<ThemeState>()

val ThemeModule = FC<PropsWithChildren> { props ->
    val state = useState(Themes.Light)
    val (theme) = state

    ThemeContext(state) {
        ThemeProvider {
            this.theme = theme

            CssBaseline()
            +props.children
        }
    }
}

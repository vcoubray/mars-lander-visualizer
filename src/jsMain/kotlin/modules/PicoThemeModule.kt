package modules


import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div

enum class PicoTheme(val value: String) {
    LIGHT("light"),
    DARK("dark");

    companion object {
        fun fromValue(value: String) =
            values().firstOrNull { it.value == value } ?: error("No Theme found for [$value]")
    }
}

var HTMLAttributes<*>.theme: PicoTheme?
    get() = PicoTheme.fromValue(asDynamic()["data-theme"] as String)
    set(value) {
        asDynamic()["data-theme"] = value?.value ?: ""
    }


typealias PicoThemeState = StateInstance<PicoTheme>

val PicoThemeContext = createContext<PicoThemeState>()

val PicoThemeModule = FC<PropsWithChildren> { props ->
    val state = useState(PicoTheme.LIGHT)
    val (theme) = state
    PicoThemeContext(state) {
        div {
            this.theme = theme
            +props.children
        }
    }
}
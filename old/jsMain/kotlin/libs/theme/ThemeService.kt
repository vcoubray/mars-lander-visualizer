package libs.theme

import kotlinx.browser.document
import kotlinx.browser.window


object ThemeService {

    fun init() {
        this.schemeMode = this.schemeMode // Yes, it's normal
        this.schemeColor = this.schemeColor // It's also normal
    }

    private val prefersSchemeMode: ThemeMode?
        get() = if (window.matchMedia("(prefers-color-scheme: dark)").matches) ThemeMode.DARK
        else null

    var schemeMode: ThemeMode
        get() = document.documentElement?.theme
            ?: window.localStorage.theme
            ?: this.prefersSchemeMode
            ?: ThemeMode.LIGHT
        set(schemeMode) {
            document.documentElement?.theme = schemeMode
            window.localStorage.theme = schemeMode
        }

    var schemeColor: ThemeColor
        get() = document.documentElement?.colorScheme
            ?: window.localStorage.colorScheme
            ?: ThemeColor.BLUE
        set(colorScheme) {
            document.documentElement?.colorScheme = colorScheme
            window.localStorage.colorScheme = colorScheme
        }

    fun toggleMode() {
        val theme = if (schemeMode == ThemeMode.DARK) ThemeMode.LIGHT else ThemeMode.DARK
        schemeMode = theme
    }

    fun changeColor(color: ThemeColor) {
       schemeColor = color
    }
}







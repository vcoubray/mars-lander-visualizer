package Theme

import kotlinx.browser.document


fun toggleDarkMode() {
    val theme = (document.documentElement?.getAttribute(THEME_ATTRIBUTE)
        ?.let(ThemeMode.Companion::fromValue)
        ?: ThemeMode.LIGHT)
        .let { if (it == ThemeMode.DARK) ThemeMode.LIGHT else ThemeMode.DARK }
    document.documentElement?.setAttribute(THEME_ATTRIBUTE, theme.value)
}

fun changeThemeColor(color: ThemeColor){
    document.documentElement?.setAttribute(THEME_COLOR_ATTRIBUTE, color.value)
}

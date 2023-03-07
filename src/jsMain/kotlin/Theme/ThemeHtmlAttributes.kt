package Theme

import react.dom.html.HTMLAttributes

const val THEME_ATTRIBUTE = "data-theme"
const val THEME_COLOR_ATTRIBUTE = "data-color"

var HTMLAttributes<*>.theme: ThemeMode?
    get() = ThemeMode.fromValue(asDynamic()[THEME_ATTRIBUTE] as String)
    set(value) {
        asDynamic()[THEME_ATTRIBUTE] = value?.value ?: ""
    }


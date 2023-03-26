package theme

import org.w3c.dom.Element
import org.w3c.dom.Storage
import react.dom.html.HTMLAttributes

const val THEME_ATTRIBUTE = "data-theme"
const val THEME_COLOR_ATTRIBUTE = "data-color"
const val THEME_MODE_STORE_KEY = "schemeModeKey"
const val THEME_COLOR_STORE_KEY = "schemeColorKey"


/* HTML ATTRIBUTES */
var HTMLAttributes<*>.theme: ThemeMode?
    get() = ThemeMode.fromValue(asDynamic()[THEME_ATTRIBUTE] as String)
    set(theme) {
        asDynamic()[THEME_ATTRIBUTE] = theme?.value ?: ""
    }

var HTMLAttributes<*>.colorScheme: ThemeColor?
    get() = ThemeColor.fromValue(asDynamic()[THEME_COLOR_ATTRIBUTE] as String)
    set(color) {
        asDynamic()[THEME_COLOR_ATTRIBUTE] = color?.value ?: ""
    }

var Element.theme: ThemeMode?
    get() = getAttribute(THEME_ATTRIBUTE)?.let(ThemeMode.Companion::fromValue)
    set(theme) = setAttribute(THEME_ATTRIBUTE, theme?.value ?: "")

var Element.colorScheme: ThemeColor?
    get() = getAttribute(THEME_COLOR_ATTRIBUTE)?.let(ThemeColor.Companion::fromValue)
    set(color) = setAttribute(THEME_COLOR_ATTRIBUTE, color?.value ?: "")


/* Local Storage */
var Storage.theme: ThemeMode?
    get() = getItem(THEME_MODE_STORE_KEY)?.let(ThemeMode.Companion::fromValue)
    set(theme) = setItem(THEME_MODE_STORE_KEY, theme?.value ?: "")

var Storage.colorScheme: ThemeColor?
    get() = getItem(THEME_COLOR_STORE_KEY)?.let(ThemeColor.Companion::fromValue)
    set(theme) = setItem(THEME_COLOR_STORE_KEY, theme?.value ?: "")

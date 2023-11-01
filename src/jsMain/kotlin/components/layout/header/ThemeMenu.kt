package components.layout.header

import web.cssom.ClassName
import mui.icons.material.Check
import mui.icons.material.Palette
import react.FC
import react.Props
import react.dom.aria.AriaHasPopup
import react.dom.aria.AriaRole
import react.dom.aria.ariaHasPopup
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.details
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.summary
import react.dom.html.ReactHTML.ul
import react.useState
import libs.theme.*

var ThemeMenu = FC<Props> {

    var themeColor by useState(ThemeService.schemeColor)
    var themeMode by useState(ThemeService.schemeMode)


    details {
        className = ClassName("theme-menu")
        role = AriaRole.list
        dir = "rtl"
        summary {
            role = AriaRole.link
            ariaHasPopup = AriaHasPopup.listbox
            Palette()
        }
        ul {
            role = AriaRole.listbox
            dir = "ltr"
            li {
                a {
                    className = ClassName("contrast theme-switcher")
                    +"Turn ${if (themeMode == ThemeMode.DARK) "off" else "on"} dark mode"
                    onClick = {
                        ThemeService.toggleMode()
                        themeMode = ThemeService.schemeMode
                    }
                }
            }
            li {
                div {
                    className = ClassName("theme-color-buttons")

                    ThemeColor.values().forEach { color ->
                        button {
                            this.colorScheme = color
                            this.theme = themeMode

                            if (color == themeColor) {
                                Check()
                            }
                            onClick = {
                                ThemeService.changeColor(color)
                                themeColor = color
                            }
                        }
                    }
                }
            }
        }
    }
}
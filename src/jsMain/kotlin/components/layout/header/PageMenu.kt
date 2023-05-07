package components.layout.header

import web.cssom.ClassName
import hooks.usePages
import mui.icons.material.Menu
import react.FC
import react.Props
import react.dom.aria.AriaHasPopup
import react.dom.aria.AriaRole
import react.dom.aria.ariaHasPopup
import react.dom.html.ReactHTML.details
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.summary
import react.dom.html.ReactHTML.ul
import react.router.dom.NavLink


val PageMenu = FC<Props> {
    val pages = usePages()

    details {
        className = ClassName("page-menu")
        role = AriaRole.list
        dir = "rtl"

        summary {
            role = AriaRole.link
            ariaHasPopup = AriaHasPopup.listbox
            Menu()
        }

        ul {
            role = AriaRole.listbox
            dir = "ltr"

            pages.filter { it.visible }.forEach { page ->
                li {
                    NavLink {
                        to = page.url
                        page.icon()
                        +page.label
                    }
                }
            }
        }
    }
}
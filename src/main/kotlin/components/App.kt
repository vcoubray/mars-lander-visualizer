package components

import component.common.AppHeader
import components.common.Sidebar
import csstype.Display
import mui.material.Box
import mui.system.sx
import react.FC
import react.Props
import react.router.dom.BrowserRouter
import react.useState
import modules.ThemeModule
import components.common.MainContent
import hooks.usePages

val App = FC<Props> {
    var open by useState(false)
    val pages = usePages()

    BrowserRouter {
        ThemeModule {
            Box {
                sx {
                    display = Display.flex
                }
                AppHeader {
                    this.open = open
                    this.onOpen = { open = true }
                }
                Sidebar {
                    this.pages = pages
                    this.open = open
                    this.onClose = { open = false }
                }
                MainContent {
                    this.pages = pages
                    this.open = open
                }
            }
        }
    }
}

package ui.common


import csstype.Display
import mui.icons.material.ChevronLeft
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.dom.aria.ariaLabel
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.nav
import react.dom.html.ReactHTML.ul
import react.router.dom.Link
import react.useState

val Menu = FC<Props> {

    var open by useState(false)

    Box {
        sx {
            display = Display.flex
        }

        AppBar {
            Toolbar {
                IconButton {
                    ariaLabel = "open"
                    edge = IconButtonEdge.start
                    onClick = { open = true }
                    mui.icons.material.Menu {}
                }
                Typography {
                    +"Mars Lander Visualizer"
                    variant = TypographyVariant.h6
                    noWrap = true
                    component = div
                }
            }
        }

        Drawer {
            this.open = open

            IconButton {
                ChevronLeft()
                onClick = { open = false }
            }

            div {
                nav {
                    ul {
                        li {
                            Link {
                                +"Visualizer"
                                to = "/visualizer"
                            }
                        }
                        li {
                            Link {
                                +"Benchmark"
                                to = "/benchmark"
                            }
                        }
                    }
                }
            }
        }
    }
}
@file:Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")

package ui

import Benchmark
import config.PUZZLES
import config.defaultSettings
import csstype.*
import emotion.react.css
import emotion.styled.styled
import kotlinx.js.jso
import mui.icons.material.BarChartSharp
import mui.icons.material.ChevronLeft
import mui.icons.material.Menu
import mui.icons.material.VisibilitySharp
import mui.material.*
import mui.material.styles.*
import mui.system.sx
import react.*
import react.dom.aria.ariaLabel
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.main
import react.router.Route
import react.router.Routes
import react.router.dom.BrowserRouter
import react.router.dom.NavLink
import ui.visualizer.Visualizer


inline operator fun Length.minus(l: Length): Length {
    return "calc($this - $l)".unsafeCast<Length>()
}

val DrawerHeader = div.styled { _, theme ->
    theme as Theme
    display = Display.flex
    alignItems = AlignItems.center
    justifyContent = JustifyContent.flexEnd
    padding = theme.spacing(0, 1)
    console.log(theme.transitions)
    +theme.mixins.toolbar.unsafeCast<Properties>()
}


external interface MainContentProps : PropsWithChildren {
    var open: Boolean
    var drawerWidth: Int
}

val MainContent = FC<MainContentProps> { props ->
    val theme by useContext(ThemeContext)

    main {
        css {
            flexGrow = 1.unsafeCast<FlexGrow>()
            padding = theme.spacing(2)
            marginLeft = if (props.open) 0.px else (-props.drawerWidth).px

            transition = if (props.open) {
                theme.transitions.create(arrayOf("margin"), jso {
                    easing = theme.transitions.easing.easeOut
                    duration = theme.transitions.duration.enteringScreen
                })
            } else {
                theme.transitions.create(arrayOf("margin"), jso {
                    easing = theme.transitions.easing.sharp
                    duration = theme.transitions.duration.leavingScreen
                })
            }
        }

        DrawerHeader {}
        +props.children
    }
}

external interface MyAppBarProps : Props {
    var open: Boolean
    var drawerWidth: Int
    var onOpen: () -> Unit
}

val MyAppBar = FC<MyAppBarProps> { props ->
    val theme by useContext(ThemeContext)

    AppBar {
        position = AppBarPosition.fixed
        sx {
            width = if (props.open) 100.pct - props.drawerWidth.px else 100.pct
            transition = if (props.open) {
                theme.transitions.create(arrayOf("margin", "width"), jso {
                    easing = theme.transitions.easing.easeOut
                    duration = theme.transitions.duration.enteringScreen
                })
            } else {
                theme.transitions.create(arrayOf("margin", "width"), jso {
                    easing = theme.transitions.easing.sharp
                    duration = theme.transitions.duration.leavingScreen
                })
            }
        }


        Toolbar {
            if (!props.open) {
                IconButton {
                    ariaLabel = "open"
                    edge = IconButtonEdge.start
                    onClick = { props.onOpen() }
                    Menu()
                }
            }
            Typography {
                +"Mars Lander Visualizer"
                variant = TypographyVariant.h6
                noWrap = true
                component = div
            }
        }
    }
}


val App = FC<Props> {
    var open by useState(false)
    val drawerWidth = 240

    BrowserRouter {
        ThemeModule {
            Box {
                sx {
                    display = Display.flex
                }

                MyAppBar {
                    this.open = open
                    this.drawerWidth = drawerWidth
                    this.onOpen = { open = true }
                }

                Drawer {
                    this.open = open
                    this.variant = DrawerVariant.persistent
                    sx {
                        width = drawerWidth.px
                        flexShrink = "0".unsafeCast<FlexShrink>()
                    }
                    DrawerHeader {
                        IconButton {
                            ChevronLeft()
                            onClick = { open = false }
                        }
                    }
                    Divider()
                    List {
                        sx {
                            width = drawerWidth.px
                            boxSizing = BoxSizing.borderBox
                        }
                        NavLink {
                            to = "/"
                            ListItemButton {
                                ListItemIcon {
                                    VisibilitySharp()
                                }
                                ListItemText {
                                    +"Visualizer"
                                }
                            }
                        }
                        NavLink {
                            to = "/benchmark"
                            ListItemButton {
                                ListItemIcon {
                                    BarChartSharp()
                                }
                                ListItemText {
                                    +"Benchmark"
                                }
                            }
                        }
                    }
                }
                MainContent {
                    this.open = open
                    this.drawerWidth = drawerWidth

                    Routes {
                        Route {
                            index = true
                            path = "/"
                            element = Visualizer.create {
                                this.puzzles = PUZZLES
                                this.algoSettings = defaultSettings
                            }
                        }
                        Route {
                            path = "/benchmark"
                            element = Benchmark.create()
                        }
                    }
                }
            }
        }
    }
}
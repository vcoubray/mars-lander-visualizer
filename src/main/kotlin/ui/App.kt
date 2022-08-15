@file:Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")

package ui

import Benchmark
import config.PUZZLES
import config.defaultSettings
import csstype.*
import emotion.react.css
import emotion.styled.styled
import mui.icons.material.BarChartSharp
import mui.icons.material.ChevronLeft
import mui.icons.material.Menu
import mui.icons.material.VisibilitySharp
import mui.material.*
import mui.material.styles.Theme
import mui.material.styles.TypographyVariant
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
        }

        DrawerHeader {}
        +props.children
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

                AppBar {
                    position = AppBarPosition.fixed

                    sx {
                        width = if (open) 100.pct - drawerWidth.px else 100.pct
                        transition = "all 225ms cubic-bezier(0, 0, 0.2, 1) 0ms".unsafeCast<Transition>()
                    }

                    Toolbar {
                        if (!open) {
                            IconButton {
                                ariaLabel = "open"
                                edge = IconButtonEdge.start
                                onClick = { open = true }
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
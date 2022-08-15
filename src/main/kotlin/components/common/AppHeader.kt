package component.common

import config.Sizes
import csstype.minus
import csstype.pct
import mui.icons.material.Menu
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.dom.aria.ariaLabel
import react.dom.html.ReactHTML
import react.useContext
import modules.ThemeContext
import mui.utils.MuiTransitions

external interface AppHeaderProps : Props {
    var open: Boolean
    var onOpen: () -> Unit
}

val AppHeader = FC<AppHeaderProps> { props ->
    val theme by useContext(ThemeContext)

    AppBar {
        position = AppBarPosition.fixed
        sx {
            width = if (props.open) 100.pct - Sizes.Sidebar.width else 100.pct
            transition = if (props.open) {
                theme.transitions.create(arrayOf("margin","width"), MuiTransitions.closeSidebarTransition(theme))
            } else {
                theme.transitions.create(arrayOf("margin","width"), MuiTransitions.openSidebarTransition(theme))
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
                component = ReactHTML.div
            }
        }
    }
}
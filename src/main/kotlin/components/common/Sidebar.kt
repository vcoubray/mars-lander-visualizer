package components.common

import csstype.*
import emotion.react.css
import emotion.styled.styled
import models.Page
import mui.icons.material.ChevronLeft
import mui.material.*
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.router.dom.NavLink
import react.router.useLocation
import mui.utils.toMuiTheme

val DrawerHeader = ReactHTML.div.styled { _, theme ->
    val muiTheme = theme.toMuiTheme()
    display = Display.flex
    alignItems = AlignItems.center
    justifyContent = JustifyContent.flexEnd
    padding = muiTheme.spacing(0, 1)
    +muiTheme.mixins.toolbar.unsafeCast<Properties>()
}


external interface SidebarProps: Props{
    var pages: Set<Page>
    var open: Boolean
    var onClose: () -> Unit
}

val Sidebar = FC<SidebarProps> { props ->
    val currentPath = useLocation().pathname

    Drawer {
        open = props.open
        variant = DrawerVariant.persistent
        sx {
            width = config.Sizes.Sidebar.width
            flexShrink = "0".unsafeCast<FlexShrink>()
        }
        DrawerHeader {
            IconButton {
                ChevronLeft()
                onClick = { props.onClose() }
            }
        }
        Divider()
        List {
            sx {
                width = config.Sizes.Sidebar.width
                boxSizing = BoxSizing.borderBox
            }
            for (page in props.pages) {
                NavLink {
                    css {
                        textDecoration = None.none
                        color = Color.currentcolor
                    }
                    to = page.url
                    ListItemButton {
                        selected = page.url == currentPath
                        ListItemIcon { page.icon() }
                        ListItemText { +page.label }
                    }
                }
            }
        }
    }
}

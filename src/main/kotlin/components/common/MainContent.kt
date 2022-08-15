package components.common

import modules.ThemeContext
import config.Sizes
import csstype.FlexGrow
import csstype.Properties
import csstype.px
import csstype.unaryMinus
import emotion.react.css
import models.Page
import react.FC
import react.Props
import react.create
import react.dom.html.ReactHTML
import react.router.Route
import react.router.Routes
import react.useContext
import mui.utils.MuiTransitions

external interface MainContentProps : Props {
    var pages: Set<Page>
    var open: Boolean
}

val MainContent = FC<MainContentProps> { props ->
    val theme by useContext(ThemeContext)

    ReactHTML.main {
        css {
            flexGrow = 1.unsafeCast<FlexGrow>()
            padding = theme.spacing(2)
            marginLeft = if (props.open) 0.px else -Sizes.Sidebar.width
            transition = if (props.open) {
                theme.transitions.create(arrayOf("margin"), MuiTransitions.closeSidebarTransition(theme))
            } else {
                theme.transitions.create(arrayOf("margin"), MuiTransitions.openSidebarTransition(theme))
            }
        }

        ReactHTML.div {
            css {
                +theme.mixins.toolbar.unsafeCast<Properties>()
            }
        }

        Routes {
            props.pages.forEachIndexed { i, page ->
//                for(page in props.pages) {
                console.log(page)
                Route {
                    index = i == 0
                    path = page.url
                    element = page.component.create()
                }
            }
        }
    }
}

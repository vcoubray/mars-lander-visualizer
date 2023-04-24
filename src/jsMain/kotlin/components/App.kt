package components


import components.layout.header.Header
import csstype.ClassName
import hooks.usePages
import react.FC
import react.Props
import react.create
import react.dom.html.ReactHTML.main
import react.router.Route
import react.router.Routes
import react.router.dom.BrowserRouter

val App = FC<Props> {
    val pages = usePages()

    BrowserRouter {
        Header ()
        main {
            className = ClassName("container-fluid")
            Routes {
                pages.forEachIndexed { i, page ->
                    Route {
                        index = i == 0
                        path = page.url
                        element = page.component.create()
                    }
                }
            }
        }
    }
}




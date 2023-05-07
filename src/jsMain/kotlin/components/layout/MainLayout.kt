package components.layout

import components.layout.header.Header
import react.FC
import react.PropsWithChildren
import react.dom.html.ReactHTML.main
import web.cssom.ClassName

val MainLayout = FC<PropsWithChildren> { props ->

    Header()
    main {
        className = ClassName("container-fluid")
        +props.children
    }
}
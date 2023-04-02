package components.layout.header

import csstype.ClassName
import react.FC
import react.Props
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.nav
import react.dom.html.ReactHTML.ul

val Header = FC<Props> {
    nav {
        className = ClassName("container-fluid")
        ul {
            li {
                +"Mars Lander Visualizer"
            }
        }

        ul {
            li {
                PageMenu()
            }
            li {
                ThemeMenu()
            }
        }

    }
}
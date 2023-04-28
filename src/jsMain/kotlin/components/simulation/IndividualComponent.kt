package components.simulation

import IndividualResult
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.article
import react.dom.html.ReactHTML.header
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.ul


external interface IndividualComponentProps : Props {
    var individual: IndividualResult
}

val IndividualComponent = FC<IndividualComponentProps> { props ->

    article {
        header {
            +"Individual"
            span { +"Score: ${props.individual.score.asDynamic().toFixed(5)}" }
            span { +"State: ${props.individual.state}" }
        }
        ul {
            for ((i, action) in props.individual.actions.withIndex()) {
                li {
                    +"$action ${if (i == props.individual.path.size - 2) "<- Crashing here" else ""}"
                }
            }
        }
    }
}
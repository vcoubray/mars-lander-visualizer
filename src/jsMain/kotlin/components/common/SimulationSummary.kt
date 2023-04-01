package components.common

import SimulationSummary
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.details
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.summary
import react.dom.html.ReactHTML.ul

external interface SimulationSummaryProps : Props {
    var summary: SimulationSummary
}


val SimulationSummaryComponent = FC<SimulationSummaryProps> { props ->


    details {
        summary{
            +"Simulation ${props.summary.id}"
        }
        ul {
            li {
               +"Best score : ${props.summary.bestScore}"
            }
            li {
               +"Duration : ${props.summary.duration}ms"
            }
            li {
               +"Generations : ${props.summary.generationCount}"
            }

        }

    }
}
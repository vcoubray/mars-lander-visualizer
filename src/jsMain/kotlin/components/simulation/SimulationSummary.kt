package components.simulation

import SimulationSummary
import mui.icons.material.VisibilitySharp
import react.FC
import react.Props
import react.dom.aria.ariaBusy
import react.dom.html.ReactHTML.article
import react.dom.html.ReactHTML.span
import react.router.dom.NavLink

external interface SimulationSummaryProps : Props {
    var summary: SimulationSummary
}


val SimulationSummaryComponent = FC<SimulationSummaryProps> { props ->

    article {
        +"Simulation ${props.summary.id}"
        span { +"Best score : ${props.summary.bestScore}" }
        span { +"Duration : ${props.summary.duration}ms" }
        span { +"Generations : ${props.summary.generationCount}" }
        when (props.summary.status) {
            SimulationStatus.PENDING -> ariaBusy = true
            SimulationStatus.COMPLETE -> NavLink {
                to = "simulations/${props.summary.id}"
                VisibilitySharp()
            }
        }
    }
}
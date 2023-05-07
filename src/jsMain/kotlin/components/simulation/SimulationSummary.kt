package components.simulation

import SimulationStatus
import SimulationSummary
import mui.icons.material.DeleteSharp
import mui.icons.material.VisibilitySharp
import react.FC
import react.Props
import react.dom.aria.AriaRole
import react.dom.aria.ariaBusy
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.article
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.span
import react.router.dom.NavLink

external interface SimulationSummaryProps : Props {
    var summary: SimulationSummary
    var onDelete: () -> Unit
}


val SimulationSummaryComponent = FC<SimulationSummaryProps> { props ->

    article {
        +"Simulation ${props.summary.id}"
        span { +"Best score : ${props.summary.bestScore.asDynamic().toFixed(5)}" }
        span { +"Duration : ${props.summary.duration}ms" }
        span { +"Generations : ${props.summary.generationCount}" }
        when (props.summary.status) {
            SimulationStatus.PENDING -> ariaBusy = true
            SimulationStatus.COMPLETE -> {
                NavLink {
                    to = "/simulations/${props.summary.id}"
                    VisibilitySharp()
                }
                a {
                    DeleteSharp()
                    onClick = { props.onDelete() }
                }
            }
        }
    }
}
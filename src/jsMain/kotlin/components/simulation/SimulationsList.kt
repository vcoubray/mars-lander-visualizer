package components.simulation

import SimulationSummary
import web.cssom.ClassName
import react.FC
import react.Props
import react.dom.html.ReactHTML.div

external interface SimulationListProps : Props {
    var simulations: List<SimulationSummary>
    var onDelete: (simulationId: Int) -> Unit
}

val SimulationList = FC<SimulationListProps> { props ->

    div {
        className = ClassName("simulation-list")
        props.simulations.forEach { simulation ->
            SimulationSummaryComponent {
                summary = simulation
                onDelete = { props.onDelete(simulation.id) }
            }
        }
    }

}
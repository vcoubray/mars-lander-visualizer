package components.pages

import Generation
import GenerationSummary
import SimulationSummary
import apis.fetchGeneration
import apis.fetchGenerations
import apis.fetchSimulation
import components.player.ProgressReaderBar
import components.simulation.GenerationComponent
import kotlinx.coroutines.launch
import kotlinx.js.get
import mainScope
import react.FC
import react.Props
import react.dom.html.ReactHTML.div

import react.router.useParams
import react.useEffectOnce
import react.useState

val SimulationVisualizer = FC<Props> {

    val simulationId = useParams()["simulationId"]!!.toInt()
    var simulation by useState<SimulationSummary?>(null)
    var generations by useState<List<GenerationSummary>>(emptyList())
    var selectedGenerationId by useState(0)

    var generation by useState<Generation?>(null)

    useEffectOnce {
        mainScope.launch {
            simulation = fetchSimulation(simulationId)
            generations = fetchGenerations(simulationId)
        }
    }

    fun changeGeneration(generationId: Int) {
        selectedGenerationId = generationId
        mainScope.launch {
            generation = fetchGeneration(simulationId, generationId)
        }

    }



    if (simulation == null) {
        div {
            +"No simulation found with id [$simulationId]"
        }
    } else {
        div {
            +"Simulation $simulationId"
        }

        +"$selectedGenerationId / ${generations.size}"
        ProgressReaderBar {
            max = generations.size
            value = selectedGenerationId
            onChange = { changeGeneration(it) }
        }


        generation?.let {
            GenerationComponent {
                this.generation = it
            }
        }


    }


}
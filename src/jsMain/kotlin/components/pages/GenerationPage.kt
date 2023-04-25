package components.pages

import Generation
import GenerationSummary
import SimulationSummary
import apis.fetchGeneration
import apis.fetchGenerations
import apis.fetchSimulation
import components.player.PlayerControls
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

val GenerationPage = FC<Props> {

    val simulationId = useParams()["simulationId"]!!.toInt()
    var simulation by useState<SimulationSummary?>(null)
    var generations by useState<List<GenerationSummary>>(emptyList())
    var selectedGenerationId by useState(0)
    var selectedGeneration by useState<Generation?>(null)

    useEffectOnce {
        mainScope.launch {
            simulation = fetchSimulation(simulationId)
            generations = fetchGenerations(simulationId)
        }
    }

    fun changeGeneration(generationId: Int) {
        selectedGenerationId = generationId
        mainScope.launch {
            selectedGeneration = fetchGeneration(simulationId, generationId)
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
        PlayerControls {
            max = generations.size
            defaultValue = selectedGenerationId
            onChange = { changeGeneration(it) }
        }

        selectedGeneration?.let {
            GenerationComponent {
                this.generation = it
                this.generationId = selectedGenerationId
            }
        }


    }


}
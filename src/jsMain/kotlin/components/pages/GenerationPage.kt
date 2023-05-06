package components.pages

import Generation
import GenerationSummary
import Puzzle
import SimulationSummary
import apis.*
import components.player.PlayerCanvas
import components.player.PlayerControls
import components.simulation.GenerationComponent
import components.simulation.IndividualComponent
import csstype.ClassName
import drawers.MarsGenerationDrawer
import kotlinx.coroutines.launch
import kotlinx.js.get
import mainScope
import react.*
import react.dom.html.ReactHTML.div

import react.router.useParams

val GenerationPage = FC<Props> {

    val simulationId = useParams()["simulationId"]!!.toInt()
    var simulation by useState<SimulationSummary?>(null)
    var puzzle by useState<Puzzle?>(null)
    var generations by useState<List<GenerationSummary>>(emptyList())
    var selectedGenerationId by useState(0)
    var selectedGeneration by useState<Generation?>(null)
    var selectedIndividualId by useState<Int?>(null)

    val marsGenerationDrawer =
        MarsGenerationDrawer(selectedGeneration, puzzle, simulation?.settings, selectedIndividualId)

    useEffectOnce {
        mainScope.launch {
            simulation = fetchSimulation(simulationId)
        }
        mainScope.launch {
            generations = fetchGenerations(simulationId)
        }
    }

    useEffect(simulation) {
        mainScope.launch {
            if (simulation != null) {
                puzzle = getPuzzle(simulation!!.settings.puzzleId)
            }
        }
    }

    fun changeGeneration(generationId: Int) {
        selectedGenerationId = generationId
        selectedIndividualId = null
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
            +"Simulation $simulationId ${puzzle?.let { " - ${it.title}" } ?: ""}"
        }

        PlayerCanvas {
            drawer = marsGenerationDrawer
        }

        PlayerControls {
            max = generations.size
            defaultValue = selectedGenerationId
            onChange = { changeGeneration(it) }
        }

        selectedGeneration?.let { generation ->
            div {
                className = ClassName("grid")
                GenerationComponent {
                    this.generation = generation
                    this.generationId = selectedGenerationId
                    this.selectedIndividualId = selectedIndividualId
                    this.onSelectIndividual = { id -> selectedIndividualId = id }
                }

                selectedIndividualId?.let { individualId ->
                    IndividualComponent {
                        individual = generation.population[individualId]
                    }
                }
            }
        }
    }
}
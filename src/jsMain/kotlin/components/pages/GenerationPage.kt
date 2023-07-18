package components.pages

import GenerationResult
import GenerationSummary
import MarsEngineSettings
import Puzzle
import SimulationSummary
import apis.fetchGeneration
import apis.fetchGenerations
import apis.fetchSimulation
import apis.getPuzzle
import components.layout.MainLayout
import components.player.PlayerCanvas
import components.player.PlayerControls
import components.simulation.GenerationComponent
import components.simulation.IndividualComponent
import drawers.MarsGenerationDrawer
import kotlinx.coroutines.launch
import mainScope
import react.*
import react.dom.html.ReactHTML.div
import react.router.useParams
import web.cssom.ClassName

val GenerationPage = FC<Props> {

    val simulationId = useParams()["simulationId"]!!.toInt()
    var simulation by useState<SimulationSummary?>(null)
    var puzzle by useState<Puzzle?>(null)
    var generations by useState<List<GenerationSummary>>(emptyList())
    var selectedGenerationId by useState(0)
    var selectedGeneration by useState<GenerationResult?>(null)
    var selectedIndividualId by useState<Int?>(null)


    val marsGenerationDrawer =
        MarsGenerationDrawer(selectedGeneration, puzzle, simulation?.simulationSettings?.engineSettings, selectedIndividualId)

    useEffectOnce {
        mainScope.launch {
            simulation = fetchSimulation(simulationId)
        }
        mainScope.launch {
            generations = fetchGenerations(simulationId)
        }
    }

    useEffect(simulation) {
        println(simulation)
        mainScope.launch {
            if (simulation != null) {
                val engine = simulation!!.simulationSettings.engineSettings as MarsEngineSettings
                puzzle = getPuzzle(engine.puzzleId)
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

    MainLayout {
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
}
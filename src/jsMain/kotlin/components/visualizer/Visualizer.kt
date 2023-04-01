package components.visualizer

import AlgoSettings
import Config
import GenerationResult
import Puzzle
import apis.algoNext
import apis.fetchPuzzles
import apis.resetAlgo
import components.common.AlgoSettings
import csstype.Display
import csstype.FlexDirection
import csstype.JustifyContent
import emotion.react.css
import kotlinx.coroutines.*
import modules.ThemeContext
import mui.material.Box
import mui.system.sx
import react.*

val mainScope = MainScope()

val Visualizer = FC<Props> {

    var populationResult: GenerationResult? by useState(null)
    var selectedChromosomeId: Int? by useState(null)
    var autoStop: Boolean by useState(true)
    val algoSettings by useState(Config.defaultSettings.copy())
    var puzzles by useState(emptyList<Puzzle>())

    var job by useState<Job?>(null)

    val theme by useContext(ThemeContext)

    useEffectOnce {
        mainScope.launch {
            populationResult = resetAlgo(algoSettings)
        }
        mainScope.launch {
            puzzles = fetchPuzzles()
        }
    }

    fun stop() {
        job?.cancel()
        job = null
    }

    fun reset(algoSettings: AlgoSettings) {
        stop()
        mainScope.launch {
            populationResult = resetAlgo(algoSettings)
            selectedChromosomeId = null
        }
    }

    useEffect(populationResult) {
        if (autoStop && (populationResult?.best ?: 0.0) >= algoSettings.maxScore()) {
            stop()
        }
    }

    Box {
        css {
            display = Display.flex
            justifyContent = JustifyContent.spaceBetween
        }
        MarsCanvas {

            this.puzzle = puzzles.getOrNull(algoSettings.puzzleId)
            this.populationResult = populationResult
            this.selectedChromosome = selectedChromosomeId?.let { populationResult?.population?.get(it) }
            this.autoStop = autoStop
            this.maxScore = algoSettings.maxScore()
        }

        Box {
            sx {
                display = Display.flex
                flexDirection = FlexDirection.column
            }
            Box {
                sx {
                    marginBottom = theme.spacing(2)
                }
                PuzzleSelector {
                    this.puzzles = puzzles
                    this.defaultValue = algoSettings.puzzleId
                    this.onPuzzleChange = { puzzleId -> reset(algoSettings.apply { this.puzzleId = puzzleId }) }
                }
            }
            AlgoSettings {
                this.algoSettings = algoSettings
                this.onUpdateSettings = { algoSettings -> reset(algoSettings) }
            }

            MediaControls {
                this.isPlaying = job?.isActive ?: false
                this.autoStop = autoStop
                this.onNext = {
                    stop()
                    mainScope.launch {
                        populationResult = algoNext()
                    }
                }
                this.onPlay = {
                    job = mainScope.launch {
                        while (this.isActive) {
                            populationResult = algoNext()
                        }
                    }
                }
                this.onStop = { stop() }
                this.onReset = { reset(algoSettings) }
                this.toggleAutoStop = { it -> autoStop = it }
            }
        }
    }
    Box {
        css {
            display = Display.flex
            justifyContent = JustifyContent.spaceBetween
        }

        PopulationList {
            this.populationResult = populationResult
            this.selectedChromosomeId = selectedChromosomeId
            this.onSelect = { chromosomeId -> selectedChromosomeId = chromosomeId }
            this.maxScore = algoSettings.maxScore()
        }

        selectedChromosomeId?.let { id ->
            console.log(selectedChromosomeId)
            ChromosomeDetail {
                this.chromosome = populationResult?.population?.get(id)!!
            }
        }
    }

}
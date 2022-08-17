package components.visualizer

import models.PopulationResult
import condigame.Chromosome
import condigame.GeneticAlgorithm
import PUZZLES
import config.Config
import csstype.*
import emotion.react.css
import kotlinx.js.timers.Timeout
import kotlinx.js.timers.clearInterval
import kotlinx.js.timers.setInterval
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.useEffectOnce
import react.useState


val Visualizer = FC<Props> {

    var intervalId: Timeout? by useState(null)
    var populationResult: PopulationResult? by useState(null)
    var selectedChromosome: Chromosome? by useState(null)
    var autoStop: Boolean by useState(true)
    var refreshRate: Int by useState(1)
    val algo by useState(GeneticAlgorithm(Config.defaultSettings))
    val algoSettings by useState(Config.defaultSettings)

    useEffectOnce {
        populationResult = algo.reset()
    }

    fun stop() {
        intervalId?.let {
            clearInterval(it)
            intervalId = null
        }
    }

    react.useEffect(populationResult) {
        if (autoStop && (populationResult?.best ?: 0.0) >= algoSettings.maxScore()) {
            stop()
        }
    }

    div {
        css {
            display = Display.flex
            justifyContent = JustifyContent.spaceBetween
        }
        MarsCanvas {
            this.puzzle = algoSettings.puzzle
            this.populationResult = populationResult
            this.selectedChromosome = selectedChromosome
            this.autoStop = autoStop
            this.maxScore = algo.settings.maxScore()
        }

        div {
            css {
                marginLeft = 10.px
            }

            AlgoSettings {
                this.puzzles = PUZZLES
                this.algoSettings = algoSettings
                this.onUpdateSettings = { algoSettings ->
                    stop()
                    populationResult = algo.updateSettings(algoSettings)
                    selectedChromosome = null
                }
            }

            MediaControls {
                this.intervalId = intervalId
                this.autoStop = autoStop
                this.refreshRate = refreshRate
                this.onNext = {
                    stop()
                    populationResult = algo.next(refreshRate)
                }
                this.onPlay = {
                    intervalId = setInterval({ populationResult = algo.next(refreshRate) }, 100)
                }
                this.onStop = {
                    stop()
                }
                this.onReset = {
                    stop()
                    populationResult = algo.reset()
                    selectedChromosome = null
                }
                this.toggleAutoStop = { it ->
                    autoStop = it
                }
                this.onUpdateRefreshRate = { it -> refreshRate = it}
            }
        }
    }
    div {
        css {
            display = Display.flex
            justifyContent = JustifyContent.spaceBetween
        }

        PopulationList {
            this.populationResult = populationResult
            this.selectedChromosome = selectedChromosome
            this.onSelect = { chromosome -> selectedChromosome = chromosome }
            this.maxScore = algo.settings.maxScore()
        }

        selectedChromosome?.let { chromosome ->
            ChromosomeDetail {
                this.chromosome = chromosome
            }
        }
    }

}
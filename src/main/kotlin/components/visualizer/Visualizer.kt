package components.visualizer

import models.AlgoResult
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
    var algoResult: AlgoResult? by useState(null)
    var selectedChromosome: Chromosome? by useState(null)
    var autoStop: Boolean by useState(true)
    var refreshRate: Int by useState(1)
    val algo by useState(GeneticAlgorithm(Config.defaultSettings))
    val algoSettings by useState(Config.defaultSettings)

    useEffectOnce {
        algoResult = algo.reset()
    }

    fun stop() {
        intervalId?.let {
            clearInterval(it)
            intervalId = null
        }
    }

    react.useEffect(algoResult) {
        if (autoStop && (algoResult?.best ?: 0.0) >= algoSettings.maxScore()) {
            stop()
        }
    }

    div {
        css {
            display = Display.flex
            justifyContent = JustifyContent.spaceBetween
        }
        MarsCanvas {
            this.algoResult = algoResult
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
                    algoResult = algo.updateSettings(algoSettings)
                    selectedChromosome = null
                }
            }

            MediaControls {
                this.intervalId = intervalId
                this.autoStop = autoStop
                this.refreshRate = refreshRate
                this.onNext = {
                    stop()
                    algoResult = algo.next(refreshRate)
                }
                this.onPlay = {
                    intervalId = setInterval({ algoResult = algo.next(refreshRate) }, 100)
                }
                this.onStop = {
                    stop()
                }
                this.onReset = {
                    stop()
                    algoResult = algo.reset()
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
            this.algoResult = algoResult
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
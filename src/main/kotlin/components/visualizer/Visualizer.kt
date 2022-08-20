package components.visualizer

import PUZZLES
import condigame.Chromosome
import config.Config
import csstype.Display
import csstype.FlexDirection
import csstype.JustifyContent
import emotion.react.css
import kotlinx.js.timers.Timeout
import kotlinx.js.timers.clearInterval
import kotlinx.js.timers.setInterval
import models.PopulationResult
import mui.material.Box
import mui.system.sx
import react.*
import services.algoService


val Visualizer = FC<Props> {

    var intervalId: Timeout? by useState(null)
    var populationResult: PopulationResult? by useState(null)
    var selectedChromosome: Chromosome? by useState(null)
    var autoStop: Boolean by useState(true)
    var refreshRate: Int by useState(1)
    val algoSettings by useState(Config.defaultSettings)



    useEffectOnce {
        populationResult = algoService.updateSettings(algoSettings)
    }

    fun stop() {
        intervalId?.let {
            clearInterval(it)
            intervalId = null
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

            this.puzzle = PUZZLES[algoSettings.puzzleId]
            this.populationResult = populationResult
            this.selectedChromosome = selectedChromosome
            this.autoStop = autoStop
            this.maxScore = algoSettings.maxScore()
        }

        Box {
            sx {
                display = Display.flex
                flexDirection = FlexDirection.column

            }

            AlgoSettings {
                this.puzzles = PUZZLES
                this.algoSettings = algoSettings
                this.onUpdateSettings = { algoSettings ->
                    stop()
                    populationResult = algoService.updateSettings(algoSettings)
                    selectedChromosome = null
                }
            }

            MediaControls {
                this.intervalId = intervalId
                this.autoStop = autoStop
                this.refreshRate = refreshRate
                this.onNext = {
                    stop()
                    populationResult = algoService.next(refreshRate)
                }
                this.onPlay = {
                    intervalId = setInterval({ populationResult = algoService.next(refreshRate) }, 100)
                }
                this.onStop = {
                    stop()
                }
                this.onReset = {
                    stop()
                    populationResult = algoService.reset()
                    selectedChromosome = null
                }
                this.toggleAutoStop = { it ->
                    autoStop = it
                }
                this.onUpdateRefreshRate = { it -> refreshRate = it}
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
            this.selectedChromosome = selectedChromosome
            this.onSelect = { chromosome -> selectedChromosome = chromosome }
            this.maxScore = algoSettings.maxScore()
        }

        selectedChromosome?.let { chromosome ->
            ChromosomeDetail {
                this.chromosome = chromosome
            }
        }
    }

}
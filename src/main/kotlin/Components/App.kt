package Components

import AlgoResult
import AlgoSettings
import Chromosome
import GeneticAlgorithm
import Puzzle
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

external interface CanvasProps : Props {
    var puzzles: List<Puzzle>
    var algoSettings: AlgoSettings
}

val App = FC<CanvasProps> { props ->

    var intervalId: Timeout? by useState(null)
    var algoResult: AlgoResult? by useState(null)
    var selectedChromosome : Chromosome? by useState(null)

    val algo by useState(GeneticAlgorithm(props.algoSettings))

    useEffectOnce {
        algoResult = algo.reset()
    }

    fun stop() {
        intervalId?.let {
            clearInterval(it)
            intervalId = null
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
        }

        div {
            css {
                marginLeft = 10.px
            }

            AlgoSettings {
                this.puzzles = props.puzzles
                this.algoSettings = props.algoSettings
                this.onUpdateSettings = { algoSettings ->
                    stop()
                    algoResult = algo.updateSettings(algoSettings)
                    selectedChromosome = null
                }
            }

            MediaControls {
                this.intervalId = intervalId
                this.onNext = {
                    stop()
                    algoResult = algo.next()
                }
                this.onPlay = {
                    intervalId = setInterval({ algoResult = algo.next() }, 100)
                }
                this.onStop = {
                    stop()
                }
                this.onReset = {
                    stop()
                    algoResult = algo.reset()
                    selectedChromosome = null
                }
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
            this.onSelect = {chromosome -> selectedChromosome = chromosome}
        }

        selectedChromosome?.let { chromosome ->
            ChromosomeDetail {
                this.chromosome = chromosome
            }
        }
    }

}
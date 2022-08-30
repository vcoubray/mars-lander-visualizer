package components.visualizer

import GenerationResult
import codingame.Chromosome
import codingame.CrossingEnum
import csstype.NamedColor
import csstype.px
import mui.material.*
import mui.system.sx
import react.FC
import react.Props

external interface PopulationListProps : Props {
    var populationResult: GenerationResult?
    var selectedChromosome: Chromosome?
    var onSelect: (Chromosome?) -> Unit
    var maxScore: Double
}


val PopulationList = FC<PopulationListProps> { props ->

    fun Chromosome.label() = "$id : ${score.asDynamic().toFixed(5)} - ${normalizedScore.asDynamic().toFixed(5)} - ${
        cumulativeScore.asDynamic().toFixed(5)
    }"

    Box {

        List {
            sx {
                maxHeight = 300.px
            }

            ListSubheader {
                +"Population"
            }

            props.populationResult?.let { result ->
                for (chromosome in result.population.sortedByDescending { it.score }) {
                    ListItemButton {
                        sx {
                            color = when {
                                props.selectedChromosome == chromosome -> NamedColor.red
                                chromosome.fitnessResult?.status == CrossingEnum.NOPE -> NamedColor.grey
                                chromosome.fitnessResult?.status == CrossingEnum.CRASH -> NamedColor.orange
                                chromosome.score < props.maxScore -> NamedColor.black
                                else -> NamedColor.green
                            }
                        }

                        selected = props.selectedChromosome == chromosome
                        key = chromosome.id.toString()
                        ListItemText { +chromosome.label() }
                        onClick = {
                            if (props.selectedChromosome?.id == chromosome.id) {
                                props.onSelect(null)
                            } else {
                                props.onSelect(chromosome)

                            }
                        }
                    }
                }
            }
        }

    }

}
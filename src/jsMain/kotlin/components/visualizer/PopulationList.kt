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
    var selectedChromosomeId: Int?
    var onSelect: (Int?) -> Unit
    var maxScore: Double
}


val PopulationList = FC<PopulationListProps> { props ->

    fun Chromosome.label() = "${score.asDynamic().toFixed(5)} - ${normalizedScore.asDynamic().toFixed(5)} - ${
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


            props.populationResult?.population?.forEachIndexed { id, chromosome ->
//                for (chromosome in result.population.sortedByDescending { it.score }) {
                ListItemButton {
                    sx {
                        color = when {
                            props.selectedChromosomeId == id -> NamedColor.red
                            chromosome.fitnessResult?.status == CrossingEnum.NOPE -> NamedColor.grey
                            chromosome.fitnessResult?.status == CrossingEnum.CRASH -> NamedColor.orange
                            chromosome.score < props.maxScore -> NamedColor.black
                            else -> NamedColor.green
                        }
                    }

                    selected = props.selectedChromosomeId == id
                    key = id.toString()
                    ListItemText { +chromosome.label() }
                    onClick = {
                        if (props.selectedChromosomeId == id) {
                            props.onSelect(null)
                        } else {
                            props.onSelect(id)

                        }
                    }
                }

            }
        }

    }

}
package components.visualizer

import AlgoResult
import Chromosome
import csstype.Cursor
import csstype.FontWeight
import csstype.NamedColor
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2

external interface PopulationListProps : Props {
    var algoResult: AlgoResult?
    var selectedChromosome: Chromosome?
    var onSelect: (Chromosome) -> Unit
    var maxScore: Double
}


val PopulationList = FC<PopulationListProps> { props ->

    div {

        h2 {
            +"Population"
        }

        props.algoResult?.let { result ->
            for (chromosome in result.population.sortedByDescending { it.score }) {
                div {

                    css {
                        cursor = Cursor.pointer
//                        if (props.selectedChromosome == chromosome) {
                            fontWeight = FontWeight.bold
//                        }
                        color = when {
                            props.selectedChromosome == chromosome -> NamedColor.red
                            chromosome.result?.status == CrossingEnum.NOPE -> NamedColor.grey
                            chromosome.result?.status == CrossingEnum.CRASH -> NamedColor.orange
                            chromosome.score < props.maxScore -> NamedColor.black
                            else -> NamedColor.green
                        }
                    }

                    key = chromosome.id.toString()
                    +"${chromosome.id} -> ${chromosome.score.asDynamic().toFixed(5)} - ${chromosome.normalizedScore.asDynamic().toFixed(5)} - ${chromosome.cumulativeScore.asDynamic().toFixed(5)}"

                    onClick = {
                        props.onSelect(chromosome)
                    }
                }
            }
        }

    }

}
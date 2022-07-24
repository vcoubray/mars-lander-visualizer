package Components

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
import react.key

external interface PopulationListProps : Props {
    var algoResult: AlgoResult?
    var selectedChromosome: Chromosome?
    var onSelect: (Chromosome) -> Unit
}


val PopulationList = FC<PopulationListProps> { props ->

    div {

        h2 {
            +"Population List"
        }

        props.algoResult?.let { result ->
            for (chromosome in result.population.sortedByDescending { it.score }) {
                div {

                    css {
                        cursor = Cursor.pointer
                        if (props.selectedChromosome == chromosome) {
                            color = NamedColor.red
                            fontWeight = FontWeight.bold
                        }
                    }

                    key = chromosome.id.toString()
                    +"${chromosome.id} -> ${chromosome.score.asDynamic().toFixed(5)} - ${chromosome.cumulativeScore.asDynamic().toFixed(5)}"

                    onClick = {
                        props.onSelect(chromosome)
                    }
                }
            }
        }

    }

}
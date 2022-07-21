package Components

import AlgoResult
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2

external interface PopulationListProps : Props {
    var algoResult: AlgoResult?
}


val PopulationList = FC<PopulationListProps> { props ->


    div {
        h2 {
            +"Population List"
        }

        props.algoResult?.let { result ->
            for (chromosome in result.population) {
                div {
                    +"${chromosome.score.asDynamic().toFixed(5)}"
                }
            }
        }

    }

}
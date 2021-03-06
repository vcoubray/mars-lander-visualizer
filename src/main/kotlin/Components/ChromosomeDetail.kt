package Components

import Chromosome
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.p

external interface ChromosomeDetailProps : Props {
    var chromosome: Chromosome
}

val ChromosomeDetail = FC<ChromosomeDetailProps> { props ->

    div {
        h2 {
            +"Details"
        }

        p {
            +"id: ${props.chromosome.id} - score : ${props.chromosome.score.asDynamic().toFixed(5)} - ${props.chromosome.state}"
        }

        var i = 0
        for (action in props.chromosome.actions) {
            div {
                +"$action ${if (i == props.chromosome.path.size-2) "<- Crashing here" else ""}"
            }
            i++
        }
    }

}
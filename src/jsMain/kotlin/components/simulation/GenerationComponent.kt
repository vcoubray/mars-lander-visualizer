package components.simulation

import Generation
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul


external interface GenerationProps : Props {
    var generation: Generation
}

val GenerationComponent = FC<GenerationProps> { props ->
    div {
        +"Population :"

        ul {
            props.generation.population.forEach { individual ->
                li {
                    +"Score : ${individual.score} - ${individual.state}"
                }
            }
        }

    }
}
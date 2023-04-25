package components.simulation

import Generation
import react.FC
import react.Props
import react.dom.html.ReactHTML.details
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.summary
import react.dom.html.ReactHTML.ul


external interface GenerationProps : Props {
    var generation: Generation
    var generationId: Int
}

val GenerationComponent = FC<GenerationProps> { props ->
    details {
        summary {
            span{+"Population : ${props.generationId}"}
            span{+"Best Score : ${props.generation.best.asDynamic().toFixed(5)}"}
            span{+"Mean Score : ${props.generation.mean.asDynamic().toFixed(5)}"}
        }

        ul {
            props.generation.population.forEach { individual ->
                li {
                    +"Score : ${individual.score} - ${individual.state}"
                }
            }
        }

    }
}
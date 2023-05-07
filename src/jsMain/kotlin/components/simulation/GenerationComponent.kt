package components.simulation

import Generation
import codingame.CrossingEnum
import react.FC
import react.Props
import react.dom.html.ReactHTML.details
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.summary
import react.dom.html.ReactHTML.ul
import web.cssom.ClassName


external interface GenerationProps : Props {
    var generation: Generation
    var generationId: Int
    var selectedIndividualId: Int?
    var onSelectIndividual: (Int?) -> Unit
}

val GenerationComponent = FC<GenerationProps> { props ->
    details {
        className = ClassName("generation")
        summary {
            span { +"Population : ${props.generationId}" }
            span { +"Best Score : ${props.generation.best.asDynamic().toFixed(5)}" }
            span { +"Mean Score : ${props.generation.mean.asDynamic().toFixed(5)}" }
        }

        ul {
            props.generation.population
                .sortedByDescending { it.score }
                .forEach { individual ->
                    li {
                        val classNames = buildList {
                            if (individual.id == props.selectedIndividualId) {
                                add("selected")
                            }
                            when (individual.fitnessResult?.status) {
                                CrossingEnum.NOPE -> add("nope")
                                CrossingEnum.CRASH -> add("crash")
                                CrossingEnum.LANDING_ZONE -> add("landing_crash")
                                CrossingEnum.SUCCESS -> add("landing")
                                else -> {}
                            }
                        }
                        className = ClassName(classNames.joinToString(" "))

                        +"Score : ${individual.score} - ${individual.state}"
                        onClick = {
                            if (props.selectedIndividualId == individual.id) {
                                props.onSelectIndividual(null)
                            } else {
                                props.onSelectIndividual(individual.id)
                            }
                        }
                    }
                }
        }
    }


}
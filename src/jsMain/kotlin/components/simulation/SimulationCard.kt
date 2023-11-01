package components.simulation

import MarsEngineSettings
import SimulationStatus
import SimulationSummary
import components.form.mappers.toLabelMapValues
import components.react.a11yDark
import components.react.ReactSyntaxHighlighter
import mui.icons.material.DeleteSharp
import mui.icons.material.VisibilitySharp
import react.FC
import react.Props
import react.dom.aria.ariaBusy
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.article
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.span
import react.router.dom.NavLink
import react.useState
import web.cssom.ClassName

external interface SimulationCardProps : Props {
    var summary: SimulationSummary
    var onDelete: () -> Unit
}


val SimulationCard = FC<SimulationCardProps> { props ->
    var close by useState(true)


    fun replacer(key: String, value: Any?): Any? {
        return if (value is Enum<*>) value.name
        else value
    }

    article {
        className = ClassName("simulation-card ${if (close) "close" else ""}")

        ReactHTML.header {

            div {
                className = ClassName("title")
                span {
                    +"Simulation ${props.summary.id} - Score : ${
                        props.summary.bestScore.asDynamic().toFixed(5)
                    } in ${props.summary.duration}ms with ${props.summary.generationCount} generations"

                }

                span {
                    className = ClassName("actions")

                    when (props.summary.status) {
                        SimulationStatus.PENDING -> ariaBusy = true
                        SimulationStatus.COMPLETE -> {
                            NavLink {
                                to = "/simulations/${props.summary.id}"
                                VisibilitySharp()
                            }
                            a {
                                DeleteSharp()
                                onClick = { props.onDelete() }
                            }
                        }
                    }
                }
            }
            onClick = { close = !close }

        }
        div {
            className = ClassName("body")


            ReactSyntaxHighlighter {
                language = "javascript"
                style = a11yDark
                +JSON.stringify(props.summary.simulationSettings.globalSettings, ::replacer, 4).replace("_1", "")

            }
            ReactSyntaxHighlighter {
                language = "javascript"
                style = a11yDark
                +JSON.stringify(props.summary.simulationSettings.engineSettings, ::replacer, 4).replace("_1", "")

            }
        }
    }
}



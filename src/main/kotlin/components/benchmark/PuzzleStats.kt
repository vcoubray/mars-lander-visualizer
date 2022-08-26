package components.benchmark

import csstype.Overflow
import csstype.px
import models.PuzzleResults
import modules.ThemeContext
import mui.icons.material.ErrorOutline
import mui.icons.material.TaskAlt
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.*

external interface PuzzleStatsProps : Props {
    var puzzleResults : StateInstance<PuzzleResults>
}

val PuzzleStats = FC<PuzzleStatsProps> {props ->

    val theme by useContext(ThemeContext)

    Box {
        sx {
            width = 300.px
            marginLeft = theme.spacing(2)
            marginBottom = theme.spacing(2)
        }
        Accordion {

            AccordionSummary {
                val success = props.puzzleResults.component1().results.none { it.executionTime > 1000 }

                sx {
                    color = if (success) theme.palette.success.main else theme.palette.error.main
                }

                val icon = if (success) TaskAlt else ErrorOutline
                icon {
                    sx { marginRight = theme.spacing(1) }
                }

                +props.puzzleResults.component1().puzzle.title
            }
            AccordionDetails {

                Stack {
                    sx {
                        maxHeight = 300.px
                        overflowY = Overflow.scroll
                    }
                    spacing = responsive(1)
                    props.puzzleResults.component1().results.forEach { r ->
                        Alert {
                            severity = if (r.executionTime > 1000) AlertColor.error else AlertColor.success
                            +"${r.generationCount} générations in ${r.executionTime}ms"
                        }
                    }
                }

            }
        }
    }

}
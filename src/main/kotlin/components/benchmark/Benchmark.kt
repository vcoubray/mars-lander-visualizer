package components.benchmark

import PUZZLES
import csstype.*
import emotion.react.css
import models.PuzzleResult
import models.PuzzleResults
import modules.ThemeContext
import mui.icons.material.Error
import mui.icons.material.ErrorOutline
import mui.icons.material.TaskAlt
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.span
import react.useContext
import kotlin.random.Random

val Benchmark = FC<Props> {

    val theme by useContext(ThemeContext)

    val results = PUZZLES.map {
        PuzzleResults(it, List(10) { PuzzleResult(it, Random.nextInt(400), Random.nextLong(4000)) })
    }


    Box {
        sx {
            display = Display.flex
            flexWrap = FlexWrap.wrap
        }

        results.forEach { result ->
            Box {
                sx {
                    width = 300.px
                    marginLeft = theme.spacing(2)
                    marginBottom = theme.spacing(2)
                }
                Accordion {

                    AccordionSummary {
                        val success = result.results.none { it.executionTime > 1000 }
                        sx {
                            color = if(success) theme.palette.success.main else theme.palette.error.main
                        }

                        val icon = if (success) TaskAlt  else ErrorOutline
                        icon {
                            sx { marginRight = theme.spacing(1) }
                        }

                        +result.puzzle.title
                    }
                    AccordionDetails {

                        Stack {
                            sx {
                                maxHeight = 300.px
                                overflowY = Overflow.scroll
                            }
                            spacing = responsive(1)
                            result.results.forEach { r ->
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
    }

}
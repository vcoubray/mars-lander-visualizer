package components.benchmark

import Puzzle
import RunStats
import csstype.*
import emotion.react.css
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import modules.ThemeContext
import mui.icons.material.ErrorOutline
import mui.icons.material.InfoOutlined
import mui.icons.material.TaskAlt
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import react.*
import react.dom.html.ReactHTML.div


external interface RunStatsProps : Props {
    var puzzle: Puzzle
    var channel: Channel<List<RunStats>>
    var runCount: Int
}

val RunStats = FC<RunStatsProps> { props ->

    val theme by useContext(ThemeContext)
    var stats by useState(emptyList<RunStats>())
    val success = stats.none { it.executionTime > 1000 }


    useEffectOnce {
        mainScope.launch {
            while (true) {
                stats = props.channel.receive()
            }
        }
    }

    Box {
        sx {
            width = 350.px
            marginLeft = theme.spacing(2)
            marginBottom = theme.spacing(2)
        }
        Accordion {
            AccordionSummary {
                sx {
                    color = when {
                        stats.isEmpty() -> theme.palette.text.secondary
                        success -> theme.palette.success.main
                        else -> theme.palette.error.main
                    }
                    Selector(".MuiAccordionSummary-content")() {
                        justifyContent = JustifyContent.spaceBetween
                        width = 100.pct
                    }
                }

                val icon = when {
                    stats.isEmpty() -> InfoOutlined
                    success -> TaskAlt
                    else -> ErrorOutline
                }
                div {
                    css {
                        display = Display.flex
                    }

                    icon {
                        sx { marginRight = theme.spacing(1) }
                    }

                    Typography {
                        sx {
                            flexShrink = 0.unsafeCast<FlexShrink>()
                        }
                        variant = TypographyVariant.subtitle1
                        +props.puzzle.title
                    }
                }

                Typography {
                    +"${stats.size}/${props.runCount}"
                }
            }
            AccordionDetails {

                Stack {
                    sx {
                        maxHeight = 300.px
                        overflowY = Overflow.scroll
                    }
                    spacing = responsive(1)
                    stats.forEach { r ->
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
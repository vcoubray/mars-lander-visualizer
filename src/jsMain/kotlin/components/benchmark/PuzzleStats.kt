package components.benchmark

import Puzzle
import RunStats
import components.pages.mainScope
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
    var timeout: Int
}

val RunStats = FC<RunStatsProps> { props ->

    val theme by useContext(ThemeContext)
    var stats by useState(emptyList<RunStats>())
    val success = stats.none { it.executionTime > props.timeout }

    val maxRun = stats.takeIf { it.isNotEmpty() }?.maxByOrNull { it.executionTime }
    val meanTime = stats.takeIf { it.isNotEmpty() }?.map { it.executionTime }?.average()?.toInt() ?: 0
    val meanGeneration = stats.takeIf { it.isNotEmpty() }?.map { it.generationCount }?.average()?.toInt() ?: 0

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
                        display = Display.flex
                        flexDirection = FlexDirection.column
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
                        justifyContent = JustifyContent.spaceBetween
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

                Typography {
                    align = TypographyAlign.right
                    variant = TypographyVariant.subtitle2
                    +"Mean : $meanGeneration in ${meanTime}ms"
                }
                maxRun ?.let {
                    Typography {
                        align = TypographyAlign.right
                        variant = TypographyVariant.subtitle2
                        +"Max : ${it.generationCount} in ${it.executionTime}ms"
                    }
                }



            }
            AccordionDetails {
                if(stats.isEmpty()) {
                    Typography {
                        sx {
                            color = theme.palette.text.secondary
                            fontStyle = FontStyle.italic
                        }
                        align = TypographyAlign.center

                        TypographyVariant.subtitle1
                        +"Click play to launch runs"
                    }
                } else {
                    Stack {
                        sx {
                            maxHeight = 300.px
                            overflowY = Overflow.scroll
                        }
                        spacing = responsive(1)
                        stats.forEach { r ->
                            Alert {
                                severity = if (r.executionTime > props.timeout) AlertColor.error else AlertColor.success
                                +"${r.generationCount} générations in ${r.executionTime}ms"
                            }
                        }
                    }
                }
            }
        }
    }
}
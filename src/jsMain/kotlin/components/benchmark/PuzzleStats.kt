package components.benchmark

import Puzzle
import PuzzleResult
import csstype.Overflow
import csstype.px
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import modules.ThemeContext
import mui.icons.material.ErrorOutline
import mui.icons.material.TaskAlt
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.*

external interface PuzzleStatsProps : Props {
    var puzzle: Puzzle
    var channel: Channel<PuzzleResult?>
}

val PuzzleStats = FC<PuzzleStatsProps> { props ->

    val theme by useContext(ThemeContext)
    var stats by useState(emptyList<PuzzleResult>())
    var lastStat : PuzzleResult? by useState(null)

    useEffectOnce {
        mainScope.launch {
            while (true) {
                lastStat = props.channel.receive()
            }
        }
    }

    useEffect(lastStat){
        if (lastStat != null) {
            stats = stats + lastStat!!
        } else {
            stats = emptyList()
        }
    }

    Box {
        sx {
            width = 300.px
            marginLeft = theme.spacing(2)
            marginBottom = theme.spacing(2)
        }
        Accordion {

            AccordionSummary {
                val success = stats.none { it.executionTime > 1000 }

                sx {
                    color = if (success) theme.palette.success.main else theme.palette.error.main
                }

                val icon = if (success) TaskAlt else ErrorOutline
                icon {
                    sx { marginRight = theme.spacing(1) }
                }

                +props.puzzle.title
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
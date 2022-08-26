package components.benchmark

import PUZZLES
import config.Config
import csstype.Display
import csstype.FlexWrap
import models.PuzzleResult
import models.PuzzleResults
import mui.icons.material.PlayArrowSharp
import mui.material.Box
import mui.material.Button
import mui.material.ButtonVariant
import mui.system.sx
import react.FC
import react.Props
import react.create
import react.useState
import services.algoService
import kotlin.random.Random

val Benchmark = FC<Props> {




    val results = PUZZLES.map {puzzle ->
        useState(PuzzleResults(puzzle , List(10) { PuzzleResult(it, Random.nextInt(400), Random.nextLong(4000)) }))
    }


    Button {
        +"Play "
        startIcon = PlayArrowSharp.create()
        variant = ButtonVariant.contained

        onClick = {

            results.forEach { state ->
                val (result, setResult) = state
                setResult(PuzzleResults(result.puzzle, List(1) {
                    val settings = Config.defaultSettings.copy(puzzleId = result.puzzle.id)
                    algoService.updateSettings(settings)
                    algoService.play(1)
                }))
            }

        }

    }

    Box {
        sx {
            display = Display.flex
            flexWrap = FlexWrap.wrap
        }

        results.forEach { result ->
            PuzzleStats {
                this.puzzleResults = result
            }
        }
    }

}
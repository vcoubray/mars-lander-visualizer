package components.visualizer

import AlgoSettings
import Puzzle
import mui.material.*
import mui.system.responsive
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.create
import react.dom.onChange

external interface AlgoSettingsProps : Props {
    var puzzles: List<Puzzle>
    var algoSettings: AlgoSettings
    var onUpdateSettings: (AlgoSettings) -> Unit
}

val AlgoSettings = FC<AlgoSettingsProps> { props ->


    Stack {
        spacing = responsive(2)

        Select {
            props.puzzles.forEachIndexed{i, puzzle ->
                MenuItem {
                    value = i
                    +puzzle.title
                }
            }

            size = Size.small
            value = props.algoSettings.puzzleId
            onChange = { event, _ ->
                console.log(event)
                props.algoSettings.puzzleId = event.target.value.unsafeCast<Int>()
                props.onUpdateSettings(props.algoSettings)
            }
        }

        TextField {
            label = Typography.create { +"Population Size" }
            variant = FormControlVariant.outlined
            size = Size.small
            defaultValue = props.algoSettings.populationSize.toString()

            onChange = { event ->
                props.algoSettings.populationSize = event.target.unsafeCast<HTMLInputElement>().value.toInt()
                props.onUpdateSettings(props.algoSettings)
            }
        }

        TextField {
            label = Typography.create { +"Chromosome Size" }
            variant = FormControlVariant.outlined
            size = Size.small
            defaultValue = props.algoSettings.chromosomeSize.toString()

            onChange = { event ->
                props.algoSettings.chromosomeSize = event.target.unsafeCast<HTMLInputElement>().value.toInt()
                props.onUpdateSettings(props.algoSettings)
            }
        }

        TextField {
            label = Typography.create { +"Mutation probability (0 to 1)" }
            variant = FormControlVariant.outlined
            size = Size.small
            defaultValue = props.algoSettings.mutationProbability.toString()

            onChange = { event ->
                props.algoSettings.mutationProbability = event.target.unsafeCast<HTMLInputElement>().value.toDouble()
                props.onUpdateSettings(props.algoSettings)
            }
        }

        TextField {
            label = Typography.create { +"Elitism (0 to 1)" }
            variant = FormControlVariant.outlined
            size = Size.small
            defaultValue = props.algoSettings.elitismPercent.toString()

            onChange = { event ->
                props.algoSettings.elitismPercent = event.target.unsafeCast<HTMLInputElement>().value.toDouble()
                props.onUpdateSettings(props.algoSettings)
            }
        }

        TextField {
            label = Typography.create { +"Speed Max" }
            variant = FormControlVariant.outlined
            size = Size.small
            defaultValue = props.algoSettings.speedMax.toString()

            onChange = { event ->
                props.algoSettings.speedMax = event.target.unsafeCast<HTMLInputElement>().value.toDouble()
                props.onUpdateSettings(props.algoSettings)
            }
        }

        TextField {
            label = Typography.create { +"X Speed weight" }
            variant = FormControlVariant.outlined
            size = Size.small
            defaultValue = props.algoSettings.xSpeedWeight.toString()

            onChange = { event ->
                props.algoSettings.xSpeedWeight = event.target.unsafeCast<HTMLInputElement>().value.toDouble()
                props.onUpdateSettings(props.algoSettings)
            }
        }

        TextField {
            label = Typography.create { +"Y Speed weight" }
            variant = FormControlVariant.outlined
            size = Size.small
            defaultValue = props.algoSettings.ySpeedWeight.toString()

            onChange = { event ->
                props.algoSettings.ySpeedWeight = event.target.unsafeCast<HTMLInputElement>().value.toDouble()
                props.onUpdateSettings(props.algoSettings)
            }
        }


        TextField {
            label = Typography.create { +"Rotate weight" }
            variant = FormControlVariant.outlined
            size = Size.small
            defaultValue = props.algoSettings.rotateWeight.toString()

            onChange = { event ->
                props.algoSettings.rotateWeight = event.target.unsafeCast<HTMLInputElement>().value.toDouble()
                props.onUpdateSettings(props.algoSettings)
            }
        }

        TextField {
            label = Typography.create { +"Distance weight" }
            variant = FormControlVariant.outlined
            size = Size.small
            defaultValue = props.algoSettings.distanceWeight.toString()

            onChange = { event ->
                props.algoSettings.distanceWeight = event.target.unsafeCast<HTMLInputElement>().value.toDouble()
                props.onUpdateSettings(props.algoSettings)
            }
        }

        TextField {
            label = Typography.create { +"Speed weight (Crashing)" }
            variant = FormControlVariant.outlined
            size = Size.small
            defaultValue = props.algoSettings.crashSpeedWeight.toString()

            onChange = { event ->
                props.algoSettings.crashSpeedWeight = event.target.unsafeCast<HTMLInputElement>().value.toDouble()
                props.onUpdateSettings(props.algoSettings)
            }
        }

    }


}
package components.pages

import ChromosomeResult
import Config
import Generation
import SimulationStatus
import SimulationSummary
import codingame.State
import components.common.AlgoSettingsForm
import components.player.PlayerControllers
import components.simulation.SimulationSummaryComponent
import components.simulation.GenerationComponent
import react.FC
import react.Props
import react.useState
import kotlin.random.Random

val Components = FC<Props> { _ ->
    val algoSettings by useState(Config.defaultSettings.copy())


    +"Components"


    PlayerControllers {
        max = 250
        defaultValue = 0
        onChange = { value -> println(value) }
    }


    AlgoSettingsForm {
        this.algoSettings = algoSettings
    }

    SimulationSummaryComponent {
        summary = SimulationSummary(0, SimulationStatus.COMPLETE, 320, 100.0, 150)
    }

    GenerationComponent {
        generation = Generation(
            population = List(80) { ChromosomeResult(emptyList(), emptyList(), State(), Random.nextDouble(100.0)) }
        )
    }
}

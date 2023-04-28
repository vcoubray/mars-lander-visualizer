package components.pages

import IndividualResult
import Config
import Generation
import SimulationStatus
import SimulationSummary
import codingame.Action
import codingame.State
import components.common.AlgoSettingsForm
import components.player.PlayerControls
import components.simulation.SimulationSummaryComponent
import components.simulation.GenerationComponent
import components.simulation.IndividualComponent
import react.FC
import react.Props
import react.useState
import kotlin.random.Random

val Components = FC<Props> { _ ->
    val algoSettings by useState(Config.defaultSettings.copy())


    +"Components"


    PlayerControls {
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
            population = List(80) { IndividualResult(emptyList(), emptyList(), State(), Random.nextDouble(100.0)) }
        )
    }

    IndividualComponent {
        individual = IndividualResult(
            List(50) { Action((-15..15).random(), (0..4).random()) },
            List(Random.nextInt(50)) { 0.0 to 0.0 },
            State(),
            Random.nextDouble(100.0)
        )
    }
}

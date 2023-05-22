package components.pages

import Config
import GenerationResult
import MarsChromosomeResult
import SimulationStatus
import SimulationSummary
import codingame.Action
import codingame.State
import components.common.AlgoSettingsForm
import components.layout.MainLayout
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


    MainLayout {
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
            summary = SimulationSummary(0, algoSettings.copy(), SimulationStatus.COMPLETE, 320, 100.0, 150)
        }

        GenerationComponent {
            generation = GenerationResult(
                population = List(80) {
                    MarsChromosomeResult(
                        it,
                        emptyList(),
                        emptyList(),
                        State(),
                        Random.nextDouble(100.0)
                    )
                }
            )
        }

        IndividualComponent {
            individual = MarsChromosomeResult(
                0,
                List(50) { Action((-15..15).random(), (0..4).random()) },
                List(Random.nextInt(50)) { 0.0 to 0.0 },
                State(),
                Random.nextDouble(100.0)
            )
        }
    }
}

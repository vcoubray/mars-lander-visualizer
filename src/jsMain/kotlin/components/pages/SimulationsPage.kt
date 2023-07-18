package components.pages

import Config
import SimulationStatus
import SimulationSummary
import apis.fetchSimulation
import apis.fetchSimulations
import apis.startSimulations
import components.common.AlgoSettingsForm
import components.layout.MainLayout
import components.simulation.SimulationList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mainScope
import mui.icons.material.RocketLaunch
import react.FC
import react.Props
import react.dom.aria.ariaBusy
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.useEffectOnce
import react.useState
import web.cssom.ClassName

val SimulationsPage = FC<Props> {

    var simulations by useState<List<SimulationSummary>>(emptyList())
    var simulationPending by useState(false)
    val simulationSettings = Config.defaultSettings.copy()

    useEffectOnce {
        mainScope.launch {
            simulations = fetchSimulations()
        }
    }

    fun startSimulation() {
        mainScope.launch {
            simulationPending = true
            val simuId = startSimulations(simulationSettings)
            simulations = fetchSimulations()
            var lastSimulation = fetchSimulation(simuId)

            while (lastSimulation.status == SimulationStatus.PENDING) {
                delay(1_000)
                lastSimulation = fetchSimulation(simuId)
            }
            simulations = fetchSimulations()
            simulationPending = false
        }
    }

    fun deleteSimulation(simulationId: Int) {
        mainScope.launch{
            apis.deleteSimulation(simulationId)
            simulations = fetchSimulations()
        }
    }


    MainLayout {
        +"Simulations"
        div {
            className = ClassName("grid")
            SimulationList {
                this.simulations = simulations
                this.onDelete = { simulationId -> deleteSimulation(simulationId)}
            }
            div {
                AlgoSettingsForm {
                    this.simulationsSettings = simulationSettings
                    this.onUpdateSettings = {
                        println(simulationSettings)
                    }
                }
                button {
                    ariaBusy = simulationPending

                    if (!simulationPending) {
                        RocketLaunch()
                    }
                    +"Launch"

                    onClick = {
                        startSimulation()
                    }
                }
            }

        }
    }
}

package components.pages

import SimulationSummary
import apis.fetchSimulation
import apis.fetchSimulations
import apis.startSimulations
import components.common.AlgoSettingsForm
import components.simulation.SimulationList
import csstype.ClassName
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mui.icons.material.RocketLaunch
import react.FC
import react.Props
import react.dom.aria.ariaBusy
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.useEffectOnce
import react.useState

val SimulationsPage = FC<Props> {

    var simulations by useState<List<SimulationSummary>>(emptyList())
    var simulationPending by useState(false)
    val algoSettings = Config.defaultSettings.copy()

    useEffectOnce {
        mainScope.launch {
            simulations = fetchSimulations()
        }
    }

    fun startSimulation(){
        mainScope.launch {
            simulationPending = true
            val simuId = startSimulations(algoSettings)
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

    +"Simulations"
    div {
        className = ClassName("grid")
        SimulationList{
            this.simulations = simulations
        }
        div {
            AlgoSettingsForm {
                this.algoSettings = algoSettings
                this.onUpdateSettings = {
                    println(algoSettings)
                }
            }
            button {
                ariaBusy = simulationPending

                if( !simulationPending ) {
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

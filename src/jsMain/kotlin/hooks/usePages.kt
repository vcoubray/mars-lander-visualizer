package hooks

import components.pages.Benchmark
import components.pages.Components
import components.pages.SimulationVisualizer
import components.pages.SimulationsPage
import components.visualizer.Visualizer
import models.Page
import mui.icons.material.*
import react.useMemo

fun usePages(): Set<Page>{
    return useMemo {
        setOf(
            Page("/", "Components", Home , Components),
            Page("/simulations", "Simulations", Rocket , SimulationsPage),
            Page("/simulations/:simulationId", "Simulations", VisibilitySharp, SimulationVisualizer, visible = false),
            Page("/components", "Components", Extension , Components),
            Page("/benchmark", "Benchmark", BarChartSharp, Benchmark),
        )
    }
}

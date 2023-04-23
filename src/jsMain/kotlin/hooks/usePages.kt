package hooks

import components.pages.Benchmark
import components.pages.Components
import components.pages.SimulationVisualizer
import components.pages.SimulationsPage
import components.visualizer.Visualizer
import models.Page
import mui.icons.material.BarChartSharp
import mui.icons.material.Extension
import mui.icons.material.Home
import mui.icons.material.VisibilitySharp
import react.useMemo

fun usePages(): Set<Page>{
    return useMemo {
        setOf(
            Page("/", "Simulations", Home , SimulationsPage),
            Page("/simulations/:simulationId", "Simulations", VisibilitySharp, SimulationVisualizer, visible = false),
            Page("/components", "Components", Extension , Components),
            Page("/benchmark", "Benchmark", BarChartSharp, Benchmark),
        )
    }
}

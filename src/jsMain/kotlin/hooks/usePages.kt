package hooks

import components.pages.Benchmark
import components.pages.Components
import components.pages.GenerationPage
import components.pages.SimulationsPage
import models.Page
import mui.icons.material.BarChartSharp
import mui.icons.material.Extension
import mui.icons.material.Rocket
import mui.icons.material.VisibilitySharp
import react.useMemo

fun usePages(): Set<Page>{
    return useMemo {
        setOf(
            Page("/", "Simulations", Rocket , SimulationsPage),
            Page("/simulations/:simulationId", "Simulations", VisibilitySharp, GenerationPage, visible = false),
            Page("/components", "Components", Extension , Components),
            Page("/benchmark", "Benchmark", BarChartSharp, Benchmark),
        )
    }
}

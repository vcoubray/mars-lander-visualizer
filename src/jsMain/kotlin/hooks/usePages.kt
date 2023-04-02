package hooks

import components.pages.Benchmark
import components.pages.Components
import components.visualizer.Visualizer
import models.Page
import mui.icons.material.BarChartSharp
import mui.icons.material.Extension
import mui.icons.material.VisibilitySharp
import react.useMemo

fun usePages(): Set<Page>{
    return useMemo {
        setOf(
            Page("/", "Components", Extension , Components),
            Page("/benchmark", "Benchmark", BarChartSharp, Benchmark),
            Page("/visualize", "Visualizer", VisibilitySharp, Visualizer)
        )
    }
}
package hooks

import models.Page
import mui.icons.material.BarChartSharp
import mui.icons.material.VisibilitySharp
import react.useMemo
import components.benchmark.Benchmark
import components.visualizer.Visualizer

fun usePages(): Set<Page>{
    return useMemo {
        setOf(
            Page("/", "Benchmark", BarChartSharp, Benchmark),
            Page("/visualize", "Visualizer", VisibilitySharp, Visualizer)
        )
    }
}
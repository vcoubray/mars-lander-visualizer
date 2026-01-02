package codingame

import BenchmarkSettings
import Puzzle
import SimulationStatus
import SimulationSummary
import kotlinx.serialization.Serializable

@Serializable
data class BenchmarkResult (
    val id: Int,
    val settings: BenchmarkSettings<*>,
    var status: SimulationStatus = SimulationStatus.PENDING,
    var runs: List<BenchmarkRun> = emptyList(),
)

@Serializable
data class BenchmarkRun(
    val puzzle: Puzzle,
    val status: SimulationStatus = SimulationStatus.PENDING,
    val simulationsSummaries: List<SimulationSummary> = emptyList()
) {
    val maxTime = simulationsSummaries.maxOf{it.duration}
}
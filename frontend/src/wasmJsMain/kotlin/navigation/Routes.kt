package navigation

import kotlinx.serialization.Serializable

object Routes {
    @Serializable object Simulations
    @Serializable object Benchhmarks
    @Serializable data class Visualization(val simulationId: Int)
    @Serializable data class BenchmarkDetail(val benchmarkId: Int)
}
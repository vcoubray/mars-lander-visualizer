package fr.vco.genetic.algorithm.visualizer

import kotlinx.serialization.Serializable

@Serializable
data class BenchmarkSettings<T : EngineSettings>(
    val puzzlesId: List<Int>,
    val runCount: Int,
    val globalSettings: GlobalSettings,
    val engineSettings: T,
)

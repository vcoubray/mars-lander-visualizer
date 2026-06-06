package fr.vco.genetic.algorithm.visualizer

import kotlinx.serialization.Serializable

@Serializable
data class MarsEngineSettings(
    var puzzleId: Int,
    var speedMax: Double,
    var xSpeedWeight: Double,
    var ySpeedWeight: Double,
    var rotateWeight: Double,
    var distanceWeight: Double,
    var crashSpeedWeight: Double,
) : EngineSettings {
    override fun maxScore() = xSpeedWeight + ySpeedWeight + rotateWeight + distanceWeight
    override fun withPuzzleId(puzzleId: Int): MarsEngineSettings = copy(puzzleId = puzzleId)
}

typealias MarsSimulationResult = SimulationResult<MarsEngineSettings>
typealias MarsSimulationSummary = SimulationSummary<MarsEngineSettings>
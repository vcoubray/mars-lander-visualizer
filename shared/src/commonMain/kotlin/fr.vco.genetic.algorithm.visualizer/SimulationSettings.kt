package fr.vco.genetic.algorithm.visualizer

import kotlinx.serialization.Serializable

interface LabeledEnum {
    val label: String
}


enum class LimitType(override val label: String): LabeledEnum {
    TIME("Time (ms)"), SCORE("Score");

}

enum class SelectionType(override val label: String): LabeledEnum {
    RANDOM("Random"),
    TOURNAMENT("Tournament"),
    ROULETTE_WHEEL("Roulette Wheel"),
}

enum class CrossoverType(override val label: String): LabeledEnum {
    BLEND("Blend"),
    SINGLE_POINT("Single Point"),
    UNIFORM("Uniform"),
}

enum class MutationType(override val label: String): LabeledEnum {
    PER_GENE("Per Gene"),
}

@Serializable
data class SimulationSettings<T : EngineSettings>(
    val globalSettings: GlobalSettings,
    val engineSettings: T,
)


@Serializable
data class GlobalSettings (
    var limitType: LimitType,
    var limitValue: Int,
    var chromosomeSize: Int,
    var populationSize: Int,
    var mutationProbability: Double,
    var elitismPercent: Double,
    var selectionType: SelectionType,
    var crossoverType: CrossoverType = CrossoverType.BLEND,
    var mutationType: MutationType = MutationType.PER_GENE,
)

@Serializable
sealed interface EngineSettings {
    fun maxScore(): Double
}

typealias MarsSettings = SimulationSettings<MarsEngineSettings>
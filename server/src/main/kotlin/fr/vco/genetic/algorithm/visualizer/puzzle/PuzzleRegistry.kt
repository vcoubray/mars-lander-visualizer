package fr.vco.genetic.algorithm.visualizer.puzzle

import fr.vco.genetic.algorithm.visualizer.EngineSettings
import fr.vco.genetic.algorithm.visualizer.Puzzle
import fr.vco.genetic.algorithm.visualizer.SimulationSettings

class PuzzleRegistry(val modules: List<PuzzleModule<*, *>>) {

    val allScenarios: List<Puzzle>
        get() = modules.flatMap { it.scenarios }

    @Suppress("UNCHECKED_CAST")
    fun <S : EngineSettings> findBySettings(settings: SimulationSettings<S>): PuzzleModule<S, *> =
        modules.firstOrNull { it.settingsClass == settings.engineSettings::class }
            as? PuzzleModule<S, *>
            ?: error("No PuzzleModule registered for ${settings.engineSettings::class.simpleName}")
}

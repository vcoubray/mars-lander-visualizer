package ui.components

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import fr.vco.genetic.algorithm.visualizer.Config
import fr.vco.genetic.algorithm.visualizer.GlobalSettings
import fr.vco.genetic.algorithm.visualizer.MarsEngineSettings
import fr.vco.genetic.algorithm.visualizer.MarsSettings
import fr.vco.genetic.algorithm.visualizer.SimulationSettings

class MarsSimulationFormState(defaults: MarsSettings = Config.defaultSettings) {

    // Global Settings
    var limitType by mutableStateOf(defaults.globalSettings.limitType)
    var limitValue by mutableStateOf(defaults.globalSettings.limitValue.toString())
    var chromosomeSize by mutableStateOf(defaults.globalSettings.chromosomeSize.toString())
    var populationSize by mutableStateOf(defaults.globalSettings.populationSize.toString())
    var mutationProbability by mutableStateOf(defaults.globalSettings.mutationProbability)
    var elitismPercent by mutableStateOf(defaults.globalSettings.elitismPercent)
    var selectionType by mutableStateOf(defaults.globalSettings.selectionType)
    var crossoverType by mutableStateOf(defaults.globalSettings.crossoverType)

    // MarsEngine Settings
    var puzzleId by mutableStateOf(defaults.engineSettings.puzzleId)
    var speedMax by mutableStateOf(defaults.engineSettings.speedMax.toString())
    var xSpeedWeight by mutableStateOf(defaults.engineSettings.xSpeedWeight.toString())
    var ySpeedWeight by mutableStateOf(defaults.engineSettings.ySpeedWeight.toString())
    var rotateWeight by mutableStateOf(defaults.engineSettings.rotateWeight.toString())
    var distanceWeight by mutableStateOf(defaults.engineSettings.distanceWeight.toString())
    var crashSpeedWeight by mutableStateOf(defaults.engineSettings.crashSpeedWeight.toString())


    val limitValueError by derivedStateOf {
        val n = limitValue.toIntOrNull() ?: return@derivedStateOf "Must be a number"
        if (n <= 0) "Must be > 0" else null
    }

    val chromosomeSizeError by derivedStateOf {
        val n = chromosomeSize.toIntOrNull() ?: return@derivedStateOf "Must be a number"
        if (n < 1) "Min 1" else null
    }

    val populationSizeError by derivedStateOf {
        val n = populationSize.toIntOrNull() ?: return@derivedStateOf "Must be a number"
        if (n < 2) "Min 2" else null
    }

    val isValid by derivedStateOf {
        limitValueError == null && chromosomeSizeError == null && populationSizeError == null
    }

    fun toSettings(): MarsSettings = SimulationSettings(
        globalSettings = GlobalSettings(
            limitType = limitType,
            limitValue = limitValue.toInt(),
            chromosomeSize = chromosomeSize.toInt(),
            populationSize = populationSize.toInt(),
            mutationProbability = mutationProbability,
            elitismPercent = elitismPercent,
            selectionType = selectionType,
            crossoverType = crossoverType,
        ),
        engineSettings = MarsEngineSettings(
            puzzleId = puzzleId,
            speedMax = speedMax.toDouble(),
            xSpeedWeight = xSpeedWeight.toDouble(),
            ySpeedWeight = ySpeedWeight.toDouble(),
            rotateWeight = rotateWeight.toDouble(),
            distanceWeight = distanceWeight.toDouble(),
            crashSpeedWeight = crashSpeedWeight.toDouble(),
        )
    )

}
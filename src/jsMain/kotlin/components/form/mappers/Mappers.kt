package components.form.mappers

import GlobalSettings
import LimitType
import MarsEngineSettings

fun MarsEngineSettings.toMapValues() = mutableMapOf(
    "puzzleId" to this.puzzleId.toString(),
    "speedMax" to this.speedMax.toString(),
    "xSpeedWeight" to this.xSpeedWeight.toString(),
    "ySpeedWeight" to this.ySpeedWeight.toString(),
    "rotateWeight" to this.rotateWeight.toString(),
    "distanceWeight" to this.distanceWeight.toString(),
    "crashSpeedWeight" to this.crashSpeedWeight.toString(),
)

fun MarsEngineSettings.toLabelMapValues() = mutableMapOf(
    "Puzzle" to this.puzzleId.toString(),
    "Speed Max" to this.speedMax.toString(),
    "X Speed Weight" to this.xSpeedWeight.toString(),
    "Y Speed Weight" to this.ySpeedWeight.toString(),
    "Rotate Weight" to this.rotateWeight.toString(),
    "Distance Weight" to this.distanceWeight.toString(),
    "Crash Speed Weight" to this.crashSpeedWeight.toString(),
)


fun Map<String, String>.toMarsEngineSettings() = MarsEngineSettings(
    puzzleId = get("puzzleId")!!.toInt(),
    speedMax = get("speedMax")!!.toDouble(),
    xSpeedWeight = get("xSpeedWeight")!!.toDouble(),
    ySpeedWeight = get("ySpeedWeight")!!.toDouble(),
    rotateWeight = get("rotateWeight")!!.toDouble(),
    distanceWeight = get("distanceWeight")!!.toDouble(),
    crashSpeedWeight = get("crashSpeedWeight")!!.toDouble(),
)

fun GlobalSettings.toMapValues() = mutableMapOf(
    "limitType" to this.limitType.toString(),
    "limitValue" to this.limitValue.toString(),
    "populationSize" to this.populationSize.toString(),
    "chromosomeSize" to this.chromosomeSize.toString(),
    "mutationProbability" to this.mutationProbability.toString(),
    "elitismPercent" to this.elitismPercent.toString(),
)

fun GlobalSettings.toLabelMapValues() =mapOf(
    "Limit Type" to this.limitType.toString(),
    "Limit Value" to this.limitValue.toString(),
    "Population Size" to this.populationSize.toString(),
    "Chromosome Size" to this.chromosomeSize.toString(),
    "Mutation Probability" to this.mutationProbability.toString(),
    "Elitism Percent" to this.elitismPercent.toString(),
)


fun Map<String, String>.toGlobalSettings() = GlobalSettings(
    limitType = LimitType.valueOf(get("limitType")!!),
    limitValue = get("limitValue")!!.toInt(),
    populationSize = get("populationSize")!!.toInt(),
    chromosomeSize = get("chromosomeSize")!!.toInt(),
    mutationProbability = get("mutationProbability")!!.toDouble(),
    elitismPercent = get("elitismPercent")!!.toDouble(),
)


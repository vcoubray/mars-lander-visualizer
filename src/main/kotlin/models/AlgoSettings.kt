package models

class AlgoSettings(
    var chromosomeSize: Int,
    var populationSize: Int,
    var mutationProbability: Double,
    var elitismPercent: Double,
    var puzzle: Puzzle,
    var speedMax: Double,
    var xSpeedWeight: Double,
    var ySpeedWeight: Double,
    var rotateWeight: Double,
    var distanceWeight: Double,
    var crashSpeedWeight: Double
) {
    fun maxScore() = xSpeedWeight + ySpeedWeight + rotateWeight + distanceWeight
}
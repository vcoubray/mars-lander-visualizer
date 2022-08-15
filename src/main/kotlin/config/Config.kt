package config

import AlgoSettings
import PUZZLES

object Config {

    val defaultSettings = AlgoSettings(
        chromosomeSize = 180,
        populationSize = 80,
        mutationProbability = 0.02,
        elitismPercent = 0.2,
        puzzle = PUZZLES.first(),
        100.0,
        30.0,
        50.0,
        10.0,
        110.0,
        0.9
    )
}
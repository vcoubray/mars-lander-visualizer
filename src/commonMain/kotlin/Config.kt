object Config {

    val defaultSettings = MarsSettings(
        chromosomeSize = 200,
        populationSize = 80,
        mutationProbability = 0.02,
        elitismPercent = 0.2,
        engineSettings = MarsEngineSettings(
            puzzleId = 0,
            100.0,
            50.0,
            50.0,
            10.0,
            250.0,
            0.9
        )
    )
}
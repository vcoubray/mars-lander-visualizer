object Config {

    val defaultSettings = MarsSettings(
        globalSettings = GlobalSettings(
            limitType = LimitType.TIME,
            limitValue = 500,
            chromosomeSize = 200,
            populationSize = 80,
            mutationProbability = 0.02,
            elitismPercent = 0.2
        ),
        engineSettings = MarsEngineSettings(
            puzzleId = 0,
            speedMax = 100.0,
            xSpeedWeight = 50.0,
            ySpeedWeight = 50.0,
            rotateWeight = 10.0,
            distanceWeight = 250.0,
            crashSpeedWeight = 0.9
        )
    )
}
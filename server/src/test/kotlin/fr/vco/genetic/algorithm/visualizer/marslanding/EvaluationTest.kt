package fr.vco.genetic.algorithm.visualizer.marslanding


import fr.vco.genetic.algorithm.visualizer.GlobalSettings
import fr.vco.genetic.algorithm.visualizer.LimitType
import fr.vco.genetic.algorithm.visualizer.MarsEngineSettings
import fr.vco.genetic.algorithm.visualizer.MarsSettings
import fr.vco.genetic.algorithm.visualizer.SelectionType
import fr.vco.genetic.algorithm.visualizer.server.services.AlgorithmFactory
import fr.vco.genetic.algorithm.visualizer.server.services.PuzzleService
import io.kotest.core.spec.style.FunSpec

import kotlin.system.measureTimeMillis
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class EvaluationTest : FunSpec({

    test("Test performance") {
        val simulationSettings = MarsSettings(
            GlobalSettings(
                limitType = LimitType.SCORE,
                limitValue = 200,
                chromosomeSize = 80,
                populationSize = 100,
                mutationProbability = 0.2,
                elitismPercent = 10.0,
                selectionType = SelectionType.RANDOM
            ),
            MarsEngineSettings(
                0,
                speedMax = 100.0,
                xSpeedWeight = 50.0,
                ySpeedWeight = 50.0,
                rotateWeight = 50.0,
                distanceWeight = 50.0,
                crashSpeedWeight = 50.0
            )
        )

        val algorithmFactory = AlgorithmFactory(PuzzleService())
        val algo = algorithmFactory.fromSettings(simulationSettings)

        measureTimeMillis {
            algo.runUntilScore(200,{})
        }.let{println("in ${it.toDuration(DurationUnit.MILLISECONDS)}")}
    }

})
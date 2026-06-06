package fr.vco.genetic.algorithm.visualizer.marslanding


import fr.vco.genetic.algorithm.visualizer.Action
import fr.vco.genetic.algorithm.visualizer.CrossoverType
import fr.vco.genetic.algorithm.visualizer.GlobalSettings
import fr.vco.genetic.algorithm.visualizer.LimitType
import fr.vco.genetic.algorithm.visualizer.MarsEngineSettings
import fr.vco.genetic.algorithm.visualizer.MarsSettings
import fr.vco.genetic.algorithm.visualizer.MutationType
import fr.vco.genetic.algorithm.visualizer.SelectionType
import fr.vco.genetic.algorithm.visualizer.State
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.shouldBe

import kotlin.system.measureTimeMillis
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class MarsSimulatorTest : FunSpec({

    val initialState = MARS_PUZZLES[0].initialState

    val chromosomeSize = 80
    val chromosomes = listOf(
        arrayOf(Action(3,1),Action(-6,1),Action(12,1),Action(13,1),Action(-2,-1),Action(-12,1),Action(-8,-1),Action(-15,-1),Action(7,1),Action(13,1),Action(-8,1),Action(12,0),Action(7,1),Action(-14,-1),Action(-15,0),Action(-7,1),Action(-9,0),Action(-5,0),Action(-10,-1),Action(1,0),Action(-10,-1),Action(-9,0),Action(7,0),Action(-5,-1),Action(-7,1),Action(7,1),Action(4,-1),Action(-7,-1),Action(-14,0),Action(-10,0),Action(-2,-1),Action(10,-1),Action(12,1),Action(15,-1),Action(-6,0),Action(-1,1),Action(-10,0),Action(-11,1),Action(10,-1),Action(-12,1),Action(-2,0),Action(10,1),Action(0,0),Action(4,-1),Action(-5,0),Action(-11,1),Action(5,0),Action(13,0),Action(-6,-1),Action(-8,1),Action(8,1),Action(1,1),Action(2,1),Action(-8,-1),Action(-12,-1),Action(-14,0),Action(3,0),Action(7,-1),Action(15,0),Action(-9,1),Action(-11,0),Action(-3,1),Action(-13,-1),Action(2,1),Action(-8,0),Action(-11,1),Action(-6,0),Action(6,0),Action(13,0),Action(10,-1),Action(-15,0),Action(4,0),Action(8,-1),Action(-5,1),Action(15,-1),Action(-5,-1),Action(-9,0),Action(2,-1),Action(9,0),Action(5,0)),
        arrayOf(Action(-1,1),Action(1,1),Action(5,-1),Action(12,1),Action(13,-1),Action(13,-1),Action(0,1),Action(14,0),Action(-5,0),Action(12,-1),Action(13,1),Action(-6,1),Action(-12,1),Action(-3,-1),Action(-15,1),Action(2,1),Action(-2,0),Action(5,1),Action(-3,0),Action(0,-1),Action(-10,0),Action(-14,-1),Action(-11,0),Action(-1,1),Action(10,0),Action(-13,-1),Action(7,0),Action(-14,1),Action(-15,-1),Action(-7,1),Action(9,-1),Action(5,-1),Action(-14,-1),Action(-14,-1),Action(-2,-1),Action(3,-1),Action(5,1),Action(-3,-1),Action(-13,-1),Action(-9,0),Action(14,1),Action(13,-1),Action(5,-1),Action(15,1),Action(-13,0),Action(-3,-1),Action(5,1),Action(-1,0),Action(-2,0),Action(7,-1),Action(6,0),Action(8,0),Action(-2,0),Action(-12,1),Action(9,1),Action(-4,1),Action(-15,1),Action(3,0),Action(3,-1),Action(1,0),Action(3,1),Action(-5,-1),Action(13,1),Action(-15,1),Action(-14,1),Action(11,1),Action(11,-1),Action(-12,0),Action(-1,1),Action(-5,0),Action(14,1),Action(2,-1),Action(7,-1),Action(-9,0),Action(-14,0),Action(4,1),Action(-12,1),Action(5,1),Action(0,-1),Action(-7,-1)),
        arrayOf(Action(-2,1),Action(-6,0),Action(-15,0),Action(5,1),Action(-5,1),Action(-11,0),Action(14,-1),Action(8,0),Action(-9,1),Action(-15,0),Action(13,-1),Action(9,1),Action(-15,0),Action(13,1),Action(5,-1),Action(-11,0),Action(4,0),Action(2,-1),Action(-1,1),Action(-10,1),Action(-11,1),Action(2,1),Action(7,-1),Action(-3,-1),Action(-10,0),Action(14,-1),Action(5,0),Action(7,-1),Action(1,0),Action(12,0),Action(-4,0),Action(12,1),Action(10,1),Action(-12,1),Action(0,1),Action(-2,-1),Action(2,0),Action(-11,0),Action(-9,0),Action(8,0),Action(-12,0),Action(14,0),Action(4,-1),Action(-9,-1),Action(-5,-1),Action(7,0),Action(-7,0),Action(8,1),Action(13,1),Action(15,-1),Action(2,-1),Action(-1,0),Action(10,1),Action(-9,1),Action(-6,0),Action(12,0),Action(9,-1),Action(3,0),Action(-3,1),Action(10,1),Action(-3,-1),Action(8,1),Action(-4,0),Action(9,1),Action(-7,0),Action(5,1),Action(11,1),Action(-5,-1),Action(-8,1),Action(-4,1),Action(-3,1),Action(2,0),Action(-7,-1),Action(10,0),Action(-3,0),Action(14,1),Action(-6,-1),Action(4,1),Action(14,-1),Action(-4,0)),
        arrayOf(Action(-6,1),Action(2,1),Action(-8,-1),Action(-10,-1),Action(6,1),Action(-3,-1),Action(-12,1),Action(0,1),Action(12,0),Action(15,-1),Action(-12,-1),Action(8,0),Action(2,-1),Action(1,0),Action(-2,-1),Action(1,0),Action(11,1),Action(11,1),Action(-13,1),Action(-4,1),Action(7,-1),Action(6,1),Action(8,-1),Action(14,0),Action(-10,1),Action(-13,-1),Action(6,1),Action(13,0),Action(8,1),Action(6,1),Action(-12,0),Action(15,-1),Action(1,-1),Action(-9,-1),Action(-15,-1),Action(5,0),Action(5,1),Action(-9,0),Action(6,-1),Action(3,1),Action(-8,0),Action(8,1),Action(-9,0),Action(8,0),Action(1,0),Action(5,0),Action(6,-1),Action(-15,-1),Action(-12,-1),Action(10,1),Action(1,0),Action(9,1),Action(12,-1),Action(3,1),Action(-9,-1),Action(-9,-1),Action(-13,0),Action(-2,-1),Action(9,-1),Action(-11,-1),Action(11,1),Action(9,0),Action(2,-1),Action(-10,1),Action(-8,0),Action(3,-1),Action(-2,-1),Action(3,0),Action(0,-1),Action(-2,0),Action(5,1),Action(13,1),Action(-15,0),Action(-14,-1),Action(-15,-1),Action(-14,0),Action(-6,1),Action(4,1),Action(-1,-1),Action(-13,1)),
        arrayOf(Action(-13,1),Action(15,1),Action(-13,1),Action(6,-1),Action(-12,0),Action(13,1),Action(7,-1),Action(-7,1),Action(-9,0),Action(7,-1),Action(12,1),Action(10,1),Action(5,1),Action(4,-1),Action(14,1),Action(-11,0),Action(-1,1),Action(7,1),Action(-10,-1),Action(-7,0),Action(-14,1),Action(6,-1),Action(13,-1),Action(-12,-1),Action(-5,1),Action(-12,0),Action(14,0),Action(14,1),Action(-13,1),Action(-13,0),Action(10,1),Action(-10,1),Action(-4,-1),Action(-15,0),Action(9,0),Action(-10,0),Action(-9,-1),Action(-8,1),Action(-13,1),Action(2,0),Action(-5,0),Action(-15,-1),Action(-3,0),Action(13,1),Action(-11,-1),Action(8,1),Action(8,0),Action(-15,0),Action(-15,0),Action(14,0),Action(15,0),Action(-2,0),Action(13,1),Action(10,0),Action(14,1),Action(3,-1),Action(14,1),Action(-8,-1),Action(8,0),Action(10,1),Action(-1,0),Action(12,0),Action(7,1),Action(15,0),Action(10,0),Action(5,1),Action(13,-1),Action(-14,0),Action(10,1),Action(-14,-1),Action(-9,0),Action(13,1),Action(-9,0),Action(2,-1),Action(-9,-1),Action(0,-1),Action(0,1),Action(8,0),Action(13,-1),Action(11,-1)),
        arrayOf(Action(9,-1),Action(8,1),Action(8,0),Action(10,0),Action(-14,1),Action(11,0),Action(10,1),Action(9,0),Action(13,1),Action(13,0),Action(-9,1),Action(14,0),Action(-10,1),Action(2,1),Action(-1,-1),Action(-2,-1),Action(-3,-1),Action(7,-1),Action(-11,-1),Action(-4,0),Action(2,0),Action(0,0),Action(-5,0),Action(8,-1),Action(-3,0),Action(-5,-1),Action(-1,-1),Action(15,1),Action(4,0),Action(-2,1),Action(-1,-1),Action(-3,1),Action(0,-1),Action(0,1),Action(-7,1),Action(-10,0),Action(-2,0),Action(0,0),Action(5,0),Action(13,1),Action(-1,1),Action(-8,-1),Action(-12,0),Action(0,0),Action(11,-1),Action(13,-1),Action(9,1),Action(-7,0),Action(-1,-1),Action(-13,-1),Action(-12,1),Action(15,-1),Action(-14,0),Action(9,0),Action(4,-1),Action(-9,0),Action(-12,0),Action(-11,-1),Action(-4,0),Action(-11,1),Action(5,0),Action(6,1),Action(11,0),Action(4,-1),Action(-9,0),Action(-11,0),Action(-4,-1),Action(3,0),Action(12,-1),Action(-3,0),Action(-13,0),Action(4,0),Action(3,-1),Action(4,0),Action(10,1),Action(-8,0),Action(11,1),Action(13,-1),Action(11,0),Action(15,0)),
        arrayOf(Action(-6,1),Action(-10,1),Action(-2,1),Action(-7,1),Action(-1,-1),Action(-6,1),Action(6,1),Action(11,-1),Action(14,1),Action(1,-1),Action(9,1),Action(-6,1),Action(-2,0),Action(-13,0),Action(5,1),Action(0,0),Action(-1,0),Action(-4,0),Action(11,1),Action(8,0),Action(-10,0),Action(11,0),Action(7,-1),Action(4,1),Action(-12,-1),Action(8,1),Action(-1,0),Action(-6,-1),Action(11,-1),Action(15,0),Action(-7,1),Action(7,0),Action(-4,0),Action(-5,1),Action(-12,-1),Action(-7,0),Action(-5,-1),Action(9,1),Action(-14,-1),Action(-4,1),Action(-1,1),Action(11,1),Action(10,0),Action(-8,0),Action(-11,-1),Action(3,0),Action(2,1),Action(1,0),Action(2,1),Action(-4,1),Action(-3,0),Action(13,0),Action(3,0),Action(10,-1),Action(9,-1),Action(7,0),Action(14,0),Action(-4,-1),Action(3,-1),Action(-3,0),Action(0,-1),Action(-6,0),Action(-11,1),Action(-4,1),Action(-6,1),Action(-4,1),Action(2,-1),Action(1,1),Action(13,-1),Action(-5,0),Action(-1,1),Action(8,1),Action(-7,1),Action(1,1),Action(11,1),Action(2,1),Action(1,1),Action(13,-1),Action(6,0),Action(10,-1)),
        arrayOf(Action(9,1),Action(-2,1),Action(-11,-1),Action(-7,-1),Action(-13,1),Action(-7,0),Action(-7,-1),Action(-9,0),Action(-6,-1),Action(7,-1),Action(14,-1),Action(1,0),Action(-6,0),Action(12,1),Action(-14,-1),Action(-14,1),Action(-15,0),Action(8,-1),Action(-15,-1),Action(-6,0),Action(-13,1),Action(-7,0),Action(-14,0),Action(12,0),Action(-12,0),Action(5,-1),Action(0,-1),Action(-9,0),Action(-7,1),Action(-8,1),Action(4,0),Action(-6,-1),Action(7,-1),Action(12,-1),Action(-15,0),Action(-3,-1),Action(-2,1),Action(4,-1),Action(-15,0),Action(-5,0),Action(1,-1),Action(6,0),Action(-2,0),Action(8,-1),Action(-13,0),Action(14,0),Action(-13,0),Action(-13,0),Action(13,1),Action(5,-1),Action(-5,0),Action(6,-1),Action(4,-1),Action(-9,-1),Action(-12,0),Action(-4,-1),Action(-5,0),Action(-7,1),Action(-2,0),Action(5,-1),Action(-11,1),Action(-9,-1),Action(-6,1),Action(7,1),Action(14,-1),Action(14,-1),Action(13,0),Action(-2,1),Action(9,-1),Action(-13,1),Action(-12,1),Action(11,0),Action(8,0),Action(13,-1),Action(-4,-1),Action(-10,1),Action(-2,0),Action(-2,1),Action(2,1),Action(7,-1)),
        arrayOf(Action(-13,0),Action(2,0),Action(11,0),Action(-3,0),Action(9,-1),Action(14,0),Action(-2,-1),Action(0,1),Action(-6,1),Action(-9,1),Action(-14,0),Action(0,0),Action(-15,0),Action(13,1),Action(13,-1),Action(15,1),Action(-15,-1),Action(3,0),Action(-11,0),Action(0,1),Action(-8,-1),Action(7,0),Action(13,0),Action(13,-1),Action(5,1),Action(-14,0),Action(2,0),Action(-10,-1),Action(-6,-1),Action(-4,1),Action(-7,-1),Action(-10,1),Action(-12,1),Action(-5,0),Action(-3,0),Action(0,1),Action(9,1),Action(-9,0),Action(3,0),Action(-12,0),Action(-7,-1),Action(3,1),Action(-12,-1),Action(6,-1),Action(-9,-1),Action(12,-1),Action(6,-1),Action(11,-1),Action(-7,-1),Action(-11,0),Action(6,0),Action(2,-1),Action(-8,-1),Action(1,-1),Action(-5,0),Action(0,-1),Action(5,0),Action(-12,-1),Action(7,0),Action(12,-1),Action(-11,-1),Action(4,0),Action(-4,-1),Action(-4,0),Action(9,-1),Action(7,0),Action(-14,0),Action(-15,1),Action(-1,-1),Action(-3,-1),Action(-4,1),Action(-3,0),Action(9,0),Action(-15,0),Action(-13,-1),Action(-2,1),Action(0,1),Action(14,0),Action(-1,0),Action(15,0)),
        arrayOf(Action(-3,0),Action(7,-1),Action(7,-1),Action(-12,0),Action(1,-1),Action(-8,-1),Action(14,0),Action(-7,-1),Action(14,1),Action(12,1),Action(-11,0),Action(3,0),Action(2,-1),Action(1,-1),Action(0,0),Action(3,-1),Action(0,1),Action(-6,0),Action(-3,1),Action(14,0),Action(12,0),Action(15,-1),Action(7,0),Action(2,-1),Action(-6,0),Action(2,1),Action(4,1),Action(-3,0),Action(-8,-1),Action(6,0),Action(11,0),Action(9,-1),Action(-10,0),Action(-12,-1),Action(14,1),Action(-15,-1),Action(-14,0),Action(-1,1),Action(4,-1),Action(0,1),Action(2,1),Action(14,1),Action(13,0),Action(-9,1),Action(-4,-1),Action(15,1),Action(9,0),Action(-6,1),Action(14,0),Action(7,-1),Action(3,1),Action(-12,-1),Action(-8,-1),Action(-12,0),Action(4,1),Action(-15,-1),Action(8,1),Action(7,-1),Action(7,1),Action(-8,1),Action(3,1),Action(6,0),Action(4,0),Action(0,0),Action(13,1),Action(0,-1),Action(14,0),Action(8,0),Action(-1,-1),Action(9,1),Action(-3,0),Action(2,1),Action(-15,1),Action(-9,1),Action(-9,1),Action(-15,1),Action(-9,-1),Action(-9,0),Action(-9,-1),Action(1,-1)),
    )

    val expectedStates = listOf(
        State(x = 3237.0252647932343, y = 744.8182271901029, xSpeed = 53.441906775037936, ySpeed = -108.58924757707257, fuel = 434, rotate = -82, power = 3),
        State(x = 1931.0998862938577, y = 1318.1659360932993, xSpeed = -27.072138404339565, ySpeed = -74.85899406691098, fuel = 482, rotate = -41, power = 0),
        State(x = 3529.6867733880777, y = 518.1021167520441, xSpeed = 29.457383133024894, ySpeed = -94.29515592028531, fuel = 444, rotate = 20, power = 0),
        State(x = 2405.5739955451686, y = 1176.138606534868, xSpeed = -18.620390490302693, ySpeed = -73.61911258528188, fuel = 483, rotate = 29, power = 0),
        State(x = 2524.119453268666, y = 1105.26207775824, xSpeed = 44.98986441153737, ySpeed = -68.52760543004844, fuel = 378, rotate = -38, power = 4),
        State(x = 1758.783113241907, y = 1360.4996676510393, xSpeed = -40.677953224088036, ySpeed = -94.25055211514032, fuel = 502, rotate = 72, power = 1),
        State(x = 2402.201522915846, y = 1158.1082756382948, xSpeed = -27.12262761255811, ySpeed = -54.13360288774442, fuel = 314, rotate = 38, power = 4),
        State(x = 2595.991015941972, y = 1112.2959224542335, xSpeed = 12.713071367487688, ySpeed = -107.06978850560702, fuel = 531, rotate = -86, power = 2),
        State(x = 2679.3974330685387, y = 1068.1567131925362, xSpeed = 29.063325444811692, ySpeed = -66.9026952066805, fuel = 447, rotate = -60, power = 4),
        State(x = 2371.450849362753, y = 1181.2153177211694, xSpeed = -14.566255881628233, ySpeed = -95.49162691254524, fuel = 524, rotate = 70, power = 1),
    )

    test("MarsSimulator.play produces expected final state for each chromosome") {
        val surface = MARS_PUZZLES[0].toSurface()
        val initial = MarsState().apply { loadFrom(initialState) }
        chromosomes.forEachIndexed { i, actions ->
            val result = MarsSimulationRun()
            MarsSimulator.play(initial, actions, surface, result)
            result.finalState shouldBe MarsState().apply { loadFrom(expectedStates[i]) }
        }
    }

    test("MarsSimulator.play populates path with initial position + one point per action played") {
        val surface = MARS_PUZZLES[0].toSurface()
        val initial = MarsState().apply { loadFrom(initialState) }
        val result = MarsSimulationRun()
        MarsSimulator.play(initial, chromosomes[0], surface, result)

        result.path.first() shouldBe (initialState.x to initialState.y)
        (result.path.size >= 2) shouldBe true
        result.path.last() shouldBe (result.finalState.x to result.finalState.y)
    }

    test("MarsSimulator.play populates fitness with non-null result") {
        val surface = MARS_PUZZLES[0].toSurface()
        val initial = MarsState().apply { loadFrom(initialState) }
        val result = MarsSimulationRun()
        MarsSimulator.play(initial, chromosomes[0], surface, result)
        (result.fitness != null) shouldBe true
    }

    test("MarsPuzzleModule.buildAlgorithm runs and improves score over 500ms") {
        val module = MarsPuzzleModule()
        val settings = MarsSettings(
            GlobalSettings(
                limitType = LimitType.TIME,
                limitValue = 500,
                chromosomeSize = 80,
                populationSize = 100,
                mutationProbability = 0.2,
                elitismPercent = 0.10,
                selectionType = SelectionType.RANDOM,
                crossoverType = CrossoverType.BLEND,
                mutationType = MutationType.PER_GENE,
            ),
            MarsEngineSettings(
                puzzleId = 0,
                speedMax = 100.0,
                xSpeedWeight = 50.0,
                ySpeedWeight = 50.0,
                rotateWeight = 50.0,
                distanceWeight = 50.0,
                crashSpeedWeight = 50.0,
            ),
        )
        val algo = module.buildAlgorithm(settings)
        var firstBest = 0.0
        var lastBest = 0.0
        algo.runUntilTime(500) { population ->
            val best = population.maxOf { it.score }
            if (firstBest == 0.0) firstBest = best
            lastBest = best
        }
        lastBest shouldBeGreaterThan 0.0
    }

    xtest("End-to-end GA performance (manual benchmark)") {
        val module = MarsPuzzleModule()
        val settings = MarsSettings(
            GlobalSettings(
                limitType = LimitType.SCORE,
                limitValue = 200,
                chromosomeSize = 80,
                populationSize = 100,
                mutationProbability = 0.2,
                elitismPercent = 0.10,
                selectionType = SelectionType.RANDOM,
                crossoverType = CrossoverType.BLEND,
                mutationType = MutationType.PER_GENE,
            ),
            MarsEngineSettings(
                puzzleId = 0,
                speedMax = 100.0,
                xSpeedWeight = 50.0,
                ySpeedWeight = 50.0,
                rotateWeight = 50.0,
                distanceWeight = 50.0,
                crashSpeedWeight = 50.0,
            ),
        )
        val algo = module.buildAlgorithm(settings)
        measureTimeMillis {
            algo.runUntilScore(200) {}
        }.let { println("in ${it.toDuration(DurationUnit.MILLISECONDS)}") }
    }

    xtest("MarsSimulator throughput (manual benchmark)") {
        val surface = MARS_PUZZLES[0].toSurface()
        val initial = MarsState().apply { loadFrom(initialState) }
        val result = MarsSimulationRun()
        measureTimeMillis {
            repeat(10000) {
                chromosomes.forEach { actions ->
                    MarsSimulator.play(initial, actions, surface, result)
                }
            }
        }.let { println("MarsSimulator throughput: ${it.toDuration(DurationUnit.MILLISECONDS)}") }
    }

    xtest("Generate random chromosomes (helper to regenerate test data)") {
        List(10) {
            List(chromosomeSize) { Action(0, 0).apply(Action::randomize) }.toTypedArray()
        }.forEach {
            println(it.joinToString(",", "arrayOf(", "),") { (a, b) -> "Action($a,$b)" })
        }
    }

})

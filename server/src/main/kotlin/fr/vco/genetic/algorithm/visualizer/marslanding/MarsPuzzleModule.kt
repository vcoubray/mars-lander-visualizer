package fr.vco.genetic.algorithm.visualizer.marslanding

import fr.vco.genetic.algorithm.visualizer.Action
import fr.vco.genetic.algorithm.visualizer.CrossingEnum
import fr.vco.genetic.algorithm.visualizer.FitnessResult
import fr.vco.genetic.algorithm.visualizer.MarsChromosomeResult
import fr.vco.genetic.algorithm.visualizer.MarsEngineSettings
import fr.vco.genetic.algorithm.visualizer.Puzzle
import fr.vco.genetic.algorithm.visualizer.SimulationSettings
import fr.vco.genetic.algorithm.visualizer.core.GeneticAlgorithm
import fr.vco.genetic.algorithm.visualizer.core.dsl.geneticAlgorithm
import fr.vco.genetic.algorithm.visualizer.puzzle.PuzzleModule
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.random.Random

class MarsPuzzleModule : PuzzleModule<MarsEngineSettings, MarsChromosome> {

    override val id = "mars-lander"
    override val routePrefix = "/mars-landing"
    override val settingsClass = MarsEngineSettings::class
    override val scenarios: List<Puzzle> = MARS_PUZZLES

    override fun buildAlgorithm(settings: SimulationSettings<MarsEngineSettings>): GeneticAlgorithm<MarsChromosome> {
        val global = settings.globalSettings
        val engine = settings.engineSettings
        val puzzle = scenarios.firstOrNull { it.id == engine.puzzleId }
            ?: error("Puzzle not found: ${engine.puzzleId}")
        val surface = puzzle.toSurface()
        val initialMarsState = MarsState().apply { loadFrom(puzzle.initialState) }
        val result = MarsSimulationRun()
        val validScore = engine.xSpeedWeight + engine.ySpeedWeight + engine.rotateWeight + engine.distanceWeight

        return geneticAlgorithm {
            populationSize = global.populationSize
            chromosomeSize = global.chromosomeSize
            elitism = global.elitismPercent

            selection(global.selectionType)

            crossover(global.crossoverType) { p1, p2, c1, c2 ->
                val weight = Random.nextDouble(0.8) + 0.1
                val opp = 1.0 - weight
                for (i in p1.actions.indices) {
                    c1.actions[i].rotate = (weight * p1.actions[i].rotate + opp * p2.actions[i].rotate).roundToInt()
                    c1.actions[i].power = (weight * p1.actions[i].power + opp * p2.actions[i].power).roundToInt()
                    c2.actions[i].rotate = (opp * p1.actions[i].rotate + weight * p2.actions[i].rotate).roundToInt()
                    c2.actions[i].power = (opp * p1.actions[i].power + weight * p2.actions[i].power).roundToInt()
                }
                c1.fitnessResult = null
                c2.fitnessResult = null
            }

            mutation(global.mutationType, global.mutationProbability) { chromosome, probability ->
                for (action in chromosome.actions) {
                    if (Random.nextDouble() < probability) action.randomize()
                }
                chromosome.fitnessResult = null
            }

            initialize { _ ->
                MarsChromosome(Array(global.chromosomeSize) { Action(0, 0).apply(Action::randomize) })
            }

            evaluate { chromosome ->
                if (chromosome.fitnessResult == null) {
                    MarsSimulator.play(initialMarsState, chromosome.actions, surface, result)
                    chromosome.fitnessResult = result.fitness
                    chromosome.path = result.path.toList()
                    chromosome.state.loadFrom(result.finalState)
                    chromosome.score = computeScore(chromosome.fitnessResult!!, engine, surface)
                    if (chromosome.score >= validScore) {
                        chromosome.fitnessResult?.status = CrossingEnum.SUCCESS
                    }
                }
            }
        }
    }

    override fun mapChromosome(chromosome: MarsChromosome, id: Int) = MarsChromosomeResult(
        id = id,
        actions = chromosome.actions.toList(),
        path = chromosome.path,
        state = chromosome.state.toState(),
        score = chromosome.score,
        normalizedScore = chromosome.normalizedScore,
        cumulativeScore = chromosome.cumulativeScore,
        fitnessResult = chromosome.fitnessResult,
    )

    override suspend fun receiveSettings(call: ApplicationCall): SimulationSettings<MarsEngineSettings> =
        call.receive()

    private fun computeScore(fitness: FitnessResult, engine: MarsEngineSettings, surface: Surface): Double {
        val speedMax = engine.speedMax
        val rotateMax = 80.0
        val distMax = surface.distanceMax
        return max(0.0, (speedMax - fitness.xSpeedOverflow) / speedMax) * engine.xSpeedWeight +
            max(0.0, (speedMax - fitness.ySpeedOverflow) / speedMax) * engine.ySpeedWeight +
            (rotateMax - fitness.rotateOverflow) / rotateMax * engine.rotateWeight +
            (distMax - fitness.distance) / distMax * engine.distanceWeight
    }
}

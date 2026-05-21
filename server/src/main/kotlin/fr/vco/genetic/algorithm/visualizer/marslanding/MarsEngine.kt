package fr.vco.genetic.algorithm.visualizer.marslanding

import fr.vco.genetic.algorithm.visualizer.core.Engine
import fr.vco.genetic.algorithm.visualizer.Action
import fr.vco.genetic.algorithm.visualizer.CrossingEnum
import fr.vco.genetic.algorithm.visualizer.FitnessResult
import fr.vco.genetic.algorithm.visualizer.State
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.random.Random

class MarsEngine(
    private val surface: Surface,
    initialState: State,
    val speedMax: Double,
    val xSpeedWeight: Double,
    val ySpeedWeight: Double,
    val rotateWeight: Double,
    val distanceWeight: Double,
) : Engine<MarsChromosome> {

    var validScore = xSpeedWeight + ySpeedWeight + rotateWeight + distanceWeight

    private val initialMarsState = MarsState().apply { loadFrom(initialState) }
    private val result = MarsSimulationResult()

    override fun simulate(chromosome: MarsChromosome) {
        MarsSimulator.play(initialMarsState, chromosome.actions, surface, result)
        chromosome.state.loadFrom(result.finalState)
        chromosome.path = result.path.toList()
    }

    override fun generateInitialPopulation(populationSize: Int, chromosomeSize: Int): Array<MarsChromosome> {
        return Array(populationSize) { generateChromosome(chromosomeSize) }
    }

    override fun generateChildrenPopulation(populationSize: Int, chromosomeSize: Int): Array<MarsChromosome> {
        return List(populationSize) {
            MarsChromosome(List(chromosomeSize) {
                Action(0, 0)
            }.toTypedArray())
        }.toTypedArray()
    }

    override fun mutate(chromosome: MarsChromosome, mutationProbability: Double) {
        for (action in chromosome.actions) {
            if (Random.nextDouble(1.0) < mutationProbability) {
                action.randomize()
            }
        }
    }

    override fun crossOver(
        parent1: MarsChromosome,
        parent2: MarsChromosome,
        children1: MarsChromosome,
        children2: MarsChromosome,
    ) {
        val weight = Random.nextDouble(0.8) + 0.1
        val oppWeight = (1 - weight)
        for (i in 0 until parent1.actions.size) {

            children1.actions[i].rotate =
                (weight * parent1.actions[i].rotate + oppWeight * parent2.actions[i].rotate).roundToInt()
            children1.actions[i].power =
                (weight * parent1.actions[i].power + oppWeight * parent2.actions[i].power).roundToInt()
            children2.actions[i].rotate =
                (oppWeight * parent1.actions[i].rotate + weight * parent2.actions[i].rotate).roundToInt()
            children2.actions[i].power =
                (oppWeight * parent1.actions[i].power + weight * parent2.actions[i].power).roundToInt()
        }
        children1.fitnessResult = null
        children2.fitnessResult = null
    }

    override fun evaluate(chromosome: MarsChromosome) {
        if (chromosome.fitnessResult == null) {
            MarsSimulator.play(initialMarsState, chromosome.actions, surface, result)
            chromosome.fitnessResult = result.fitness
            chromosome.path = result.path.toList()
            chromosome.state.loadFrom(result.finalState)
            chromosome.score = computeScore(chromosome.fitnessResult!!)
            if (chromosome.score >= validScore) {
                chromosome.fitnessResult?.status = CrossingEnum.SUCCESS
            }
        }
    }

    private fun computeScore(fitnessResult: FitnessResult): Double {
        val xSpeedMax = speedMax
        val ySpeedMax = speedMax
        val rotateMax = 80.0
        val distanceMax = surface.distanceMax

        val normalizedXSpeed = max(0.0, (xSpeedMax - fitnessResult.xSpeedOverflow) / xSpeedMax)
        val normalizedYSpeed = max(0.0, (ySpeedMax - fitnessResult.ySpeedOverflow) / ySpeedMax)
        val normalizedRotate = (rotateMax - fitnessResult.rotateOverflow) / rotateMax
        val normalizedDistance = (distanceMax - fitnessResult.distance) / distanceMax

        return normalizedXSpeed * xSpeedWeight + normalizedYSpeed * ySpeedWeight + normalizedRotate * rotateWeight + normalizedDistance * distanceWeight
    }


    private fun generateChromosome(chromosomeSize: Int): MarsChromosome {
        return MarsChromosome(
            List(chromosomeSize) { Action(0, 0).apply(Action::randomize) }.toTypedArray()
        )
    }

}

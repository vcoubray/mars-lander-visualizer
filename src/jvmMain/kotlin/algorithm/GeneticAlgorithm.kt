package algorithm

import codingame.*
import condigame.Surface
import condigame.play
import kotlinx.serialization.Serializable
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.random.Random


enum class SimulationStatus {
    PENDING,
    COMPLETE
}

@Serializable
data class SimulationResult(
    var status: SimulationStatus = SimulationStatus.PENDING,
    var duration: Long = 0,
    var bestScore: Double = 0.0,
    var generations: List<Generation> = emptyList(),
)

@Serializable
data class SimulationSummary(
    var status: SimulationStatus = SimulationStatus.PENDING,
    var duration: Long = 0,
    var bestScore: Double = 0.0,
    var generationCount: Int
)


@Serializable
data class Generation(
    val population: List<ChromosomeResult>,
) {
    val best = population.takeIf { it.isNotEmpty() }?.map { it.score }?.maxOrNull() ?: 0.0
    val mean = population.takeIf { it.isNotEmpty() }?.map { it.score }?.average() ?: 0.0
}
@Serializable
data class GenerationSummary(
    val populationSize: Int,
    val best: Double,
    val mean: Double
)

@Serializable
class ChromosomeResult(
    val actions: List<Action>,
    val path: List<Pair<Double, Double>>,
    val score: Double = 0.0,
    val normalizedScore: Double = 0.0,
    val cumulativeScore: Double,
    val state: State ,
    val fitnessResult: FitnessResult?
)

fun Chromosome.toResult() = ChromosomeResult(
    actions = this.actions.map { it.copy() },
    path = this.path.map { it.copy() },
    score = score,
    normalizedScore = normalizedScore,
    cumulativeScore = cumulativeScore,
    state = state.copy(),
    fitnessResult = fitnessResult
)


interface GeneticAlgorithm {
    fun runUntilTime(duration: Long): List<Generation>
    fun runUntilScore(score: Double): List<Generation>
}


class GeneticAlgorithmImpl(
    val surface: Surface,
    val initialState: State,
    val chromosomeSize: Int,
    val populationSize: Int,
    val mutationProbability: Double,
    elitismPercent: Double,
    val speedMax: Double,
    val xSpeedWeight: Double,
    val ySpeedWeight: Double,
    val rotateWeight: Double,
    val distanceWeight: Double,
) : GeneticAlgorithm {

    var population = generateRandomPopulation()
    private var children = List(populationSize) {
        Chromosome(List(chromosomeSize) {
            Action(0, 0)
        }.toTypedArray())
    }.toTypedArray()

    private val workingState = initialState.copy()

    private var eliteSize: Int
    private val childrenSize: Int
    private var bestChromosome: Chromosome
    private var scoreSum = 0.0

    init {
        eliteSize = (populationSize * elitismPercent).toInt()
        if (eliteSize % 2 != 0) {
            eliteSize += 1
        }
        childrenSize = populationSize - eliteSize
        bestChromosome = population.first()
        evaluation()
    }

    private fun generateChromosome(): Chromosome {
        return Chromosome(
            List(chromosomeSize) { Action(0, 0).apply(Action::randomize) }.toTypedArray()
        )
    }

    private fun generateRandomPopulation(): Array<Chromosome> {
        return Array(populationSize) { generateChromosome() }
    }

    private fun evaluation() {
        scoreSum = 0.0
        population.forEach {
            if (it.fitnessResult == null) {
                workingState.loadFrom(initialState)
                it.fitnessResult = workingState.play(it.actions, surface)
                it.path = workingState.path.toMutableList()
                it.state.loadFrom(workingState, true)
                it.score = getScore(it.fitnessResult!!)
                if (it.score > bestChromosome.score) {
                    bestChromosome = it
                }
            }
            scoreSum += it.score
        }


    }

    fun cumulativeScore() {

        for (chromosome in population) {
            chromosome.normalizedScore = chromosome.score / scoreSum
        }
        population.sortBy { it.normalizedScore }
        var cumul = 0.0
        for (chromosome in population) {

            cumul += chromosome.normalizedScore
            chromosome.cumulativeScore = cumul

        }
    }


    fun getScore(fitnessResult: FitnessResult): Double {
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


    fun wheelSelection(): Int {
//        val rnd = Random.nextDouble(0.80) + 0.20
        val rnd = Random.nextDouble(1.0)
        var i = population.lastIndex
        while (i >= 0 && rnd < population[i].cumulativeScore) {
            i--
        }
        return i + 1
    }

    fun randomSelection(): Int {
        return Random.nextInt(populationSize / 2) + populationSize / 2
    }


    fun selection(): Int {
//        return wheelSelection()
        return randomSelection()
    }

    fun CrossoverAndMutate(parent1: Chromosome, parent2: Chromosome, children1: Chromosome, children2: Chromosome) {

        val weight = Random.nextDouble(0.8) + 0.1
        val oppWeight = (1 - weight)
        for (i in 0 until chromosomeSize) {

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

        mutation(children1)
        mutation(children2)

    }

    fun mutation(chromosome: Chromosome) {
        for (action in chromosome.actions) {
            if (Random.nextDouble(1.0) < mutationProbability) {
                action.randomize()
            }
        }
    }


    fun nextGeneration() {

        for (i in 0 until childrenSize / 2) {

            val parentId1 = selection()
            var parentId2 = -1
            while (parentId2 == -1 || parentId2 == parentId1) {
                parentId2 = selection()
            }
            CrossoverAndMutate(
                population[parentId1],
                population[parentId2],
                children[i * 2],
                children[i * 2 + 1]
            )
        }

        for (i in childrenSize until populationSize) {
            children[i] = population[i]
        }

        val temp = population
        population = children
        children = temp

    }


    override fun runUntilScore(score: Double): List<Generation> {

        val generations = mutableListOf(Generation(population.map { it.toResult() }))
        while (bestChromosome.score < score) {
            next()
            generations.add(Generation(population.map { it.toResult() }))
        }
        return generations
    }

    override fun runUntilTime(duration: Long): List<Generation> {
        val generations = mutableListOf(Generation(population.map { it.toResult() }))
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < duration) {
            next()
            generations.add(Generation(population.map { it.toResult() }))
        }
        return generations

    }

    @Synchronized
    fun next() {
//        cumulativeScore()
        population.sortBy { it.score }
        nextGeneration()
        evaluation()
    }


    @Synchronized
    fun search(scoreMax: Double): Int {
        var generationCount = 0
        while (bestChromosome.score < scoreMax) {
            next()
            generationCount++
        }
        return generationCount
    }


}
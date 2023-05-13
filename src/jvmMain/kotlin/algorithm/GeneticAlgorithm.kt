package algorithm

import IndividualResult
import Generation
import codingame.*
import condigame.Surface
import condigame.play
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.random.Random





fun Chromosome.toResult(id: Int) = IndividualResult(
    id = id,
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

    var validScore = xSpeedWeight + ySpeedWeight + rotateWeight + distanceWeight
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
                if(it.score >= validScore) {
                    it.fitnessResult?.status = CrossingEnum.SUCCESS
                }
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


        val generations = mutableListOf(population.toGeneration())
        while (bestChromosome.score < score) {
            next()
            generations.add(population.toGeneration())
        }
        return generations
    }

    override fun runUntilTime(duration: Long): List<Generation> {
        val generations = mutableListOf(population.toGeneration())
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < duration) {
            next()
            generations.add(population.toGeneration())
        }
        return generations

    }

    private fun Array<Chromosome>.toGeneration() =
        Generation(this.mapIndexed { i, chrom -> chrom.toResult(i) })

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
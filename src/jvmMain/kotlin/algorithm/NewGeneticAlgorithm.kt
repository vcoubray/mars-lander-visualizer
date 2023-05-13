package algorithm

import Generation
import codingame.*
import condigame.Surface
import condigame.play
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.random.Random


interface IChromosome {
    fun getScore(): Double
}

data class MarsChromosome(val actions: Array<Action>) : IChromosome {
    var score = 0.0
    var normalizedScore = 0.0
    var cumulativeScore = 0.0
    var path = emptyList<Pair<Double, Double>>()
    var state: State = State()
    var fitnessResult: FitnessResult? = null

    override fun getScore(): Double {
        return score
    }
}


class NewGeneticAlgorithmImpl<T: IChromosome>(
    val engine: Engine<T>,
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

    var population = engine.generateInitialPopulation(populationSize, chromosomeSize)
    private var children = engine.generateChildrenPopulation(populationSize, chromosomeSize)

    private var eliteSize: Int
    private val childrenSize: Int
    private var bestChromosome: T
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

    private fun evaluation() {
        scoreSum = 0.0
        population.forEach {
            engine.evaluate(it)

            if (it.getScore() > bestChromosome.getScore()) {
                bestChromosome = it
            }
            scoreSum += it.getScore()
        }
    }

    fun randomSelection(): Int {
        return Random.nextInt(populationSize / 2) + populationSize / 2
    }


    fun selection(): Int {
//        return wheelSelection()
        return randomSelection()
    }

    fun CrossoverAndMutate(parent1: T, parent2: T, children1: T, children2: T) {
       engine.crossOver(parent1, parent2, children1, children2)
        mutation(children1)
        mutation(children2)
    }

    fun mutation(chromosome: T) {
        engine.mutate(chromosome, mutationProbability)
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

    @Synchronized
    override fun runUntilScore(score: Double): List<Generation> {

        val generations = mutableListOf(population.toGeneration())
        while (bestChromosome.getScore() < score) {
            next()
            generations.add(population.toGeneration())
        }
        return generations
    }

    @Synchronized
    override fun runUntilTime(duration: Long): List<Generation> {
        val generations = mutableListOf(population.toGeneration())
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < duration) {
            next()
            generations.add(population.toGeneration())
        }
        return generations

    }

    private fun Array<T>.toGeneration() =
        Generation(this.mapIndexed { i, chrom -> chrom.toResult(i) })


    fun next() {
//        cumulativeScore()
        population.sortBy { it.getScore() }
        nextGeneration()
        evaluation()
    }




}
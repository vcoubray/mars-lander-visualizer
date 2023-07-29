package algorithm

import kotlin.math.min
import kotlin.random.Random

const val MAX_TIME_LIMIT = 2000


interface GeneticAlgorithm<T : Chromosome> {
    fun runUntilTime(duration: Int, onNewGeneration: (Array<T>) -> Unit)
    fun runUntilScore(score: Int, onNewGeneration: (Array<T>) -> Unit)
}

class GeneticAlgorithmImpl<T : Chromosome>(
    val engine: Engine<T>,
    val chromosomeSize: Int,
    val populationSize: Int,
    val mutationProbability: Double,
    elitismPercent: Double,
) : GeneticAlgorithm<T> {

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

            if (it.score > bestChromosome.score) {
                bestChromosome = it
            }
            scoreSum += it.score
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
    override fun runUntilScore(score: Int, onNewGeneration: (Array<T>) -> Unit) {
        val timeout = MAX_TIME_LIMIT

        onNewGeneration(population)
        val start = System.currentTimeMillis()
        while (bestChromosome.score <= score && System.currentTimeMillis() - start < timeout) {
            next()
            onNewGeneration(population)
        }
    }

    @Synchronized
    override fun runUntilTime(duration: Int, onNewGeneration: (Array<T>) -> Unit) {
        val timeout = min(duration, MAX_TIME_LIMIT )

        onNewGeneration(population)
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < timeout) {
            next()
            onNewGeneration(population)
        }
    }

    fun next() {
//        cumulativeScore()
        population.sortBy { it.score }
        nextGeneration()
        evaluation()
    }
}
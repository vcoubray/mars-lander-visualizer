package fr.vco.genetic.algorithm.visualizer.core

import fr.vco.genetic.algorithm.visualizer.core.strategies.SelectionStrategy
import kotlin.math.min

@Suppress("UNCHECKED_CAST")
private fun <T> makeArray(size: Int, init: (Int) -> T): Array<T> {
    val arr = arrayOfNulls<Any>(size)
    for (i in arr.indices) arr[i] = init(i)
    return arr as Array<T>
}

interface GeneticAlgorithm<T : Chromosome> {
    fun runUntilTime(duration: Int, onNewGeneration: (Array<T>) -> Unit)
    fun runUntilScore(score: Int, onNewGeneration: (Array<T>) -> Unit)
}

class GeneticAlgorithmImpl<T : Chromosome>(
    val timeLimit: Int,
    val chromosomeSize: Int,
    val populationSize: Int,
    private val mutationProbability: Double,
    elitismPercent: Double,
    private val selectionStrategy: SelectionStrategy<T>,
    private val crossoverFn: (T, T, T, T) -> Unit,
    private val mutationFn: (T, Double) -> Unit,
    private val initFn: (Int) -> T,
    private val evaluateFn: (T) -> Unit,
) : GeneticAlgorithm<T> {

    var population = makeArray(populationSize, initFn)
    private var children = makeArray(populationSize, initFn)

    private val eliteSize: Int
    private val childrenSize: Int
    private var bestChromosome: T
    private var scoreSum = 0.0

    init {
        var elite = (populationSize * elitismPercent).toInt()
        if (elite % 2 != 0) elite += 1
        eliteSize = elite
        childrenSize = populationSize - eliteSize
        bestChromosome = population.first()
        evaluation()
    }

    private fun evaluation() {
        scoreSum = 0.0
        population.forEach {
            evaluateFn(it)
            if (it.score > bestChromosome.score) bestChromosome = it
            scoreSum += it.score
        }
    }

    private fun selection(): Int = selectionStrategy.selectIndex(population)

    private fun crossoverAndMutate(parent1: T, parent2: T, children1: T, children2: T) {
        crossoverFn(parent1, parent2, children1, children2)
        mutationFn(children1, mutationProbability)
        mutationFn(children2, mutationProbability)
    }

    private fun nextGeneration() {
        for (i in 0 until childrenSize / 2) {
            val parentId1 = selection()
            var parentId2 = -1
            while (parentId2 == -1 || parentId2 == parentId1) {
                parentId2 = selection()
            }
            crossoverAndMutate(
                population[parentId1],
                population[parentId2],
                children[i * 2],
                children[i * 2 + 1],
            )
        }
        for (i in childrenSize until populationSize) {
            children[i] = population[i]
        }
        val temp = population
        population = children
        children = temp
    }

    private fun next() {
        population.sortBy { it.score }
        nextGeneration()
        evaluation()
    }

    @Synchronized
    override fun runUntilScore(score: Int, onNewGeneration: (Array<T>) -> Unit) {
        onNewGeneration(population)
        val start = System.currentTimeMillis()
        while (bestChromosome.score <= score && System.currentTimeMillis() - start < timeLimit) {
            next()
            onNewGeneration(population)
        }
    }

    @Synchronized
    override fun runUntilTime(duration: Int, onNewGeneration: (Array<T>) -> Unit) {
        val timeout = min(duration, timeLimit)
        onNewGeneration(population)
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < timeout) {
            next()
            onNewGeneration(population)
        }
    }
}

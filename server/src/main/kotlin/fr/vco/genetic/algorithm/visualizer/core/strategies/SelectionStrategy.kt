package fr.vco.genetic.algorithm.visualizer.core.strategies

import fr.vco.genetic.algorithm.visualizer.core.Chromosome
import kotlin.random.Random

fun interface SelectionStrategy<T : Chromosome> {
    fun selectIndex(population: Array<T>): Int
}

class RandomSelection<T : Chromosome> : SelectionStrategy<T> {
    override fun selectIndex(population: Array<T>): Int =
        Random.nextInt(population.size / 2) + population.size / 2
}

class TournamentSelection<T : Chromosome>(private val size: Int = 5) : SelectionStrategy<T> {
    override fun selectIndex(population: Array<T>): Int =
        (0 until size)
            .map { Random.nextInt(population.size) }
            .maxBy { population[it].score }
}

class RouletteWheelSelection<T : Chromosome> : SelectionStrategy<T> {
    override fun selectIndex(population: Array<T>): Int {
        val total = population.sumOf { it.score }
        if (total <= 0.0) return Random.nextInt(population.size)
        var threshold = Random.nextDouble() * total
        for (i in population.indices) {
            threshold -= population[i].score
            if (threshold <= 0.0) return i
        }
        return population.lastIndex
    }
}

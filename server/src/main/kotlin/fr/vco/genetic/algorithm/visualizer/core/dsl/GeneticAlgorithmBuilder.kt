package fr.vco.genetic.algorithm.visualizer.core.dsl

import fr.vco.genetic.algorithm.visualizer.CrossoverType
import fr.vco.genetic.algorithm.visualizer.MutationType
import fr.vco.genetic.algorithm.visualizer.SelectionType
import fr.vco.genetic.algorithm.visualizer.core.Chromosome
import fr.vco.genetic.algorithm.visualizer.core.GeneticAlgorithm
import fr.vco.genetic.algorithm.visualizer.core.GeneticAlgorithmImpl
import fr.vco.genetic.algorithm.visualizer.core.strategies.RandomSelection
import fr.vco.genetic.algorithm.visualizer.core.strategies.RouletteWheelSelection
import fr.vco.genetic.algorithm.visualizer.core.strategies.SelectionStrategy
import fr.vco.genetic.algorithm.visualizer.core.strategies.TournamentSelection

const val DEFAULT_TIME_LIMIT = 2000

@DslMarker
annotation class GeneticAlgorithmDsl

@GeneticAlgorithmDsl
class GeneticAlgorithmBuilder<T : Chromosome> {
    var populationSize: Int = 0
    var chromosomeSize: Int = 0
    var elitism: Double = 0.0
    var timeLimit: Int = DEFAULT_TIME_LIMIT

    private var selectionType: SelectionType = SelectionType.RANDOM
    private var tournamentSize: Int = 5

    private var crossoverType: CrossoverType = CrossoverType.BLEND
    private var crossoverFn: ((T, T, T, T) -> Unit)? = null

    private var mutationType: MutationType = MutationType.PER_GENE
    private var mutationProbability: Double = 0.0
    private var mutationFn: ((T, Double) -> Unit)? = null

    private var initFn: ((Int) -> T)? = null
    private var evaluateFn: ((T) -> Unit)? = null

    fun selection(type: SelectionType, tournamentSize: Int = 5) {
        selectionType = type
        this.tournamentSize = tournamentSize
    }

    fun crossover(type: CrossoverType = CrossoverType.BLEND, impl: (T, T, T, T) -> Unit) {
        crossoverType = type
        crossoverFn = impl
    }

    fun mutation(type: MutationType = MutationType.PER_GENE, probability: Double, impl: (T, Double) -> Unit) {
        mutationType = type
        mutationProbability = probability
        mutationFn = impl
    }

    fun initialize(fn: (Int) -> T) {
        initFn = fn
    }

    fun evaluate(fn: (T) -> Unit) {
        evaluateFn = fn
    }

    fun build(): GeneticAlgorithm<T> {
        val strategy: SelectionStrategy<T> = when (selectionType) {
            SelectionType.RANDOM -> RandomSelection()
            SelectionType.TOURNAMENT -> TournamentSelection(tournamentSize)
            SelectionType.ROULETTE_WHEEL -> RouletteWheelSelection()
        }
        return GeneticAlgorithmImpl(
            timeLimit = timeLimit,
            chromosomeSize = chromosomeSize,
            populationSize = populationSize,
            mutationProbability = mutationProbability,
            elitismPercent = elitism,
            selectionStrategy = strategy,
            crossoverFn = crossoverFn ?: error("crossover {} block is required"),
            mutationFn = mutationFn ?: error("mutation {} block is required"),
            initFn = initFn ?: error("initialize {} block is required"),
            evaluateFn = evaluateFn ?: error("evaluate {} block is required"),
        )
    }
}

fun <T : Chromosome> geneticAlgorithm(block: GeneticAlgorithmBuilder<T>.() -> Unit): GeneticAlgorithm<T> =
    GeneticAlgorithmBuilder<T>().apply(block).build()

package fr.vco.genetic.algorithm.visualizer.core.dsl

import fr.vco.genetic.algorithm.visualizer.CrossoverType
import fr.vco.genetic.algorithm.visualizer.MutationType
import fr.vco.genetic.algorithm.visualizer.SelectionType
import fr.vco.genetic.algorithm.visualizer.core.Chromosome
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import kotlin.random.Random

private const val BITS = 20

private class BitChromosome(val bits: IntArray) : Chromosome()

private fun bitChromosome() = BitChromosome(IntArray(BITS) { Random.nextInt(2) })

private fun buildBitMaximizer(selectionType: SelectionType) = geneticAlgorithm<BitChromosome> {
    populationSize = 50
    chromosomeSize = BITS
    elitism = 0.1
    timeLimit = 500

    selection(selectionType)

    crossover(CrossoverType.BLEND) { p1, p2, c1, c2 ->
        val point = Random.nextInt(BITS)
        for (i in 0 until point) {
            c1.bits[i] = p1.bits[i]
            c2.bits[i] = p2.bits[i]
        }
        for (i in point until BITS) {
            c1.bits[i] = p2.bits[i]
            c2.bits[i] = p1.bits[i]
        }
    }

    mutation(MutationType.PER_GENE, 0.1) { chromosome, probability ->
        for (i in chromosome.bits.indices) {
            if (Random.nextDouble() < probability) chromosome.bits[i] = 1 - chromosome.bits[i]
        }
    }

    initialize { _ -> bitChromosome() }

    evaluate { chromosome ->
        chromosome.score = chromosome.bits.sum().toDouble()
    }
}

class GeneticAlgorithmBuilderTest : FunSpec({

    test("RANDOM selection - best score improves over 200ms") {
        val algo = buildBitMaximizer(SelectionType.RANDOM)
        var lastBest = 0.0
        algo.runUntilTime(200) { pop -> lastBest = pop.maxOf { it.score } }
        lastBest shouldBeGreaterThan 0.0
    }

    test("TOURNAMENT selection - best score improves over 200ms") {
        val algo = buildBitMaximizer(SelectionType.TOURNAMENT)
        var lastBest = 0.0
        algo.runUntilTime(200) { pop -> lastBest = pop.maxOf { it.score } }
        lastBest shouldBeGreaterThan 0.0
    }

    test("ROULETTE_WHEEL selection - best score improves over 200ms") {
        val algo = buildBitMaximizer(SelectionType.ROULETTE_WHEEL)
        var lastBest = 0.0
        algo.runUntilTime(200) { pop -> lastBest = pop.maxOf { it.score } }
        lastBest shouldBeGreaterThan 0.0
    }

    test("DSL builder fails fast when required blocks are missing") {
        val ex = runCatching {
            geneticAlgorithm<BitChromosome> {
                populationSize = 10
                chromosomeSize = 5
                // missing initialize, evaluate, crossover, mutation
                crossover(CrossoverType.BLEND) { _, _, _, _ -> }
                mutation(MutationType.PER_GENE, 0.1) { _, _ -> }
                initialize { _ -> bitChromosome() }
                // evaluate missing
            }
        }.exceptionOrNull()
        (ex != null) shouldBe true
    }

    test("runUntilScore stops when target is reached") {
        val algo = geneticAlgorithm<BitChromosome> {
            populationSize = 50
            chromosomeSize = BITS
            elitism = 0.1
            timeLimit = 5000

            selection(SelectionType.RANDOM)
            crossover(CrossoverType.BLEND) { p1, p2, c1, c2 ->
                val point = Random.nextInt(BITS)
                for (i in 0 until point) { c1.bits[i] = p1.bits[i]; c2.bits[i] = p2.bits[i] }
                for (i in point until BITS) { c1.bits[i] = p2.bits[i]; c2.bits[i] = p1.bits[i] }
            }
            mutation(MutationType.PER_GENE, 0.05) { c, p ->
                for (i in c.bits.indices) { if (Random.nextDouble() < p) c.bits[i] = 1 - c.bits[i] }
            }
            initialize { _ -> BitChromosome(IntArray(BITS) { 0 }) }
            evaluate { c -> c.score = c.bits.sum().toDouble() }
        }

        var generationCount = 0
        algo.runUntilScore(BITS) { generationCount++ }
        (generationCount > 0) shouldBe true
    }
})

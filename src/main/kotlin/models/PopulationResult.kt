package models

import condigame.Chromosome


class PopulationResult(
    val population: Array<Chromosome>,
    val generation: Int
) {
    val best = population.takeIf { it.isNotEmpty() }?.map { it.score }?.maxOrNull() ?: 0.0
    val mean = population.takeIf { it.isNotEmpty() }?.map { it.score }?.average() ?: 0.0
}


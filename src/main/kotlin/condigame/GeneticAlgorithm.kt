package condigame

import kotlin.jvm.Synchronized
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.random.Random


class GeneticAlgorithm(
    val surface: Surface,
    val initialState: State,
    val chromosomeSize: Int,
    val populationSize: Int,
    val mutationProbability: Double,
    val elitismPercent: Double,
    val speedMax: Double,
    val xSpeedWeight: Double,
    val ySpeedWeight: Double,
    val rotateWeight: Double,
    val distanceWeight: Double,
    val crashSpeedWeight: Double
) {

    var chromosomeIndex = 0
    var population = generateRandomPopulation()

    init {
        evaluation()
    }


    fun generateChromosome(): Chromosome {
        return Chromosome(
            chromosomeIndex++,
            (0 until chromosomeSize).map {
                Action.generate()
            }.toTypedArray()
        )
    }

    fun generateRandomPopulation(): Array<Chromosome> {
        return Array(populationSize) { generateChromosome() }
    }

    fun evaluation() {
        population.forEach {
            val state = initialState.copy()
            it.result = state.play(it.actions, surface)
            it.path = state.path
            it.state = state
        }

        population.forEach {
            it.score = getScore(it.result!!)
        }
        cumulativeScores()
    }

    fun getScore(result: Result): Double {
        val xSpeedMax = speedMax
        val ySpeedMax = speedMax
        val rotateMax = 80.0
        val distanceMax = surface.distanceMax

        val normalizedXSpeed = max(0.0, (xSpeedMax - result.xSpeedOverflow) / xSpeedMax)
        val normalizedYSpeed = max(0.0, (ySpeedMax - result.ySpeedOverflow) / ySpeedMax)
        val normalizedRotate = (rotateMax - result.rotateOverflow) / rotateMax
        val normalizedDistance = (distanceMax - result.distance) / distanceMax

        if (result.status != CrossingEnum.LANDING_ZONE) {
            return normalizedDistance * distanceWeight + (normalizedYSpeed * ySpeedWeight + normalizedXSpeed * xSpeedWeight) * crashSpeedWeight
        }
        return normalizedXSpeed * xSpeedWeight + normalizedYSpeed * ySpeedWeight + normalizedRotate * rotateWeight + normalizedDistance * distanceWeight
    }

    fun normalizeScores() {
        val sum = population.sumOf { it.score }
        for (chromosome in population) {
            chromosome.normalizedScore = chromosome.score / sum
        }
    }

    fun cumulativeScores() {
        normalizeScores()
        population.sortBy { it.normalizedScore }
        var cumul = 0.0
        for (chromosome in population) {
            cumul += chromosome.normalizedScore
            chromosome.cumulativeScore = cumul
        }
    }


    fun wheelSelection(): Chromosome {
        val rnd = Random.nextDouble(1.0)
        var i = 0
        while (i < population.size && rnd > population[i].cumulativeScore) {
            i++
        }

        return population[i]
    }


    fun crossOver(parent1: Chromosome, parent2: Chromosome): Pair<Chromosome, Chromosome> {

        val child1Actions = mutableListOf<Action>()
        val child2Actions = mutableListOf<Action>()

        val weight = Random.nextDouble(0.8) + 0.1
        for (i in 0 until chromosomeSize) {

            val rotate1 = weight * parent1.actions[i].rotate + (1.0 - weight) * parent2.actions[i].rotate
            val power1 = weight * parent1.actions[i].power + (1.0 - weight) * parent2.actions[i].power
            val rotate2 = (1.0 - weight) * parent1.actions[i].rotate + weight * parent2.actions[i].rotate
            val power2 = (1.0 - weight) * parent1.actions[i].power + weight * parent2.actions[i].power
            child1Actions.add(Action(rotate1.roundToInt(), power1.roundToInt()))
            child2Actions.add(Action(rotate2.roundToInt(), power2.roundToInt()))
        }

        return Chromosome(chromosomeIndex++, child1Actions.toTypedArray()) to Chromosome(
            chromosomeIndex++,
            child2Actions.toTypedArray()
        )

    }

    fun mutation(chromosome: Chromosome) {
        for (i in chromosome.actions.indices) {
            if (Random.nextDouble(1.0) < mutationProbability) {
                chromosome.actions[i] = Action.generate()
            }
        }
    }

    fun nextGeneration() {

        var eliteSize = (populationSize * elitismPercent).toInt()
        if (eliteSize % 2 != 0) {
            eliteSize++
        }

        val children = mutableListOf<Chromosome>()
        while (children.size < populationSize - eliteSize) {
            val parent1 = wheelSelection()
            var parent2: Chromosome? = null
            while (parent2 == null || parent2.id == parent1.id) {
                parent2 = wheelSelection()
            }
            val (child1, child2) = crossOver(parent1, parent2).also { (a, b) -> mutation(a);mutation(b) }
            children.add(child1)
            children.add(child2)
        }
        population = population.takeLast(eliteSize).toTypedArray() + children.toTypedArray()
    }

    @Synchronized
    fun next() {
        nextGeneration()
        evaluation()
    }


    @Synchronized
    fun search(scoreMax: Double): Int {
        var generationCount = 0
        console.log(this)
        while (population.maxOf { it.score } < scoreMax) {
            next()
            generationCount++
        }
        return generationCount
    }


}
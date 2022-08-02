import kotlin.jvm.Synchronized
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.random.Random

class Chromosome(var id: Int, var actions: Array<Action>) {
    var score = 0.0
    var normalizedScore = 0.0
    var cumulativeScore = 0.0
    var path = emptyList<Pair<Double, Double>>()
    var state: State? = null
    var result: Result? = null

    override fun equals(other: Any?): Boolean {
        return if (other is Chromosome) id == other.id
        else false
    }

    override fun hashCode(): Int {
        return id
    }
}


class AlgoSettings(
    var chromosomeSize: Int,
    var populationSize: Int,
    var mutationProbability: Double,
    var elitismPercent: Double,
    var puzzle: Puzzle,
    var speedMax: Double,
    var xSpeedWeight: Double,
    var ySpeedWeight: Double,
    var rotateWeight: Double,
    var distanceWeight: Double
) {

    fun maxScore() = xSpeedWeight + ySpeedWeight + rotateWeight + distanceWeight
}

class GeneticAlgorithm(
    var settings: AlgoSettings
) {

    var population = generateRandomPopulation()
    var generationCount = 0
    var chromosomeIndex = 0

    fun updateSettings(settings: AlgoSettings): AlgoResult {
        this.settings = settings
        return reset()
    }

    fun reset(): AlgoResult {
        generationCount = 0
        chromosomeIndex = 0
        population = generateRandomPopulation()
        return AlgoResult(this.settings.puzzle.surface, this.population, this.generationCount)
    }

    fun generateChromosome(): Chromosome {
        return Chromosome(
            chromosomeIndex++,
            (0 until settings.chromosomeSize).map {
                generateAction()
            }.toTypedArray()
        )
    }

    fun generateRandomPopulation(): Array<Chromosome> {
        return Array(settings.populationSize) { generateChromosome() }
    }

    fun evaluation() {
        population.forEach {
            val state = settings.puzzle.initialState.copy()
            it.result = state.play(it.actions, settings.puzzle.surfacePath)
            it.score = getScore(it.result!!)
            it.path = state.path
            it.state = state
        }
    }

    fun getScore(result: Result): Double {
        val xSpeedMax = settings.speedMax
        val ySpeedMax = settings.speedMax
        val rotateMax = 90.0
        val distanceMax = settings.puzzle.surfacePath.distanceMax

        val normalizedXSpeed = max(0.0, (xSpeedMax - result.xSpeedOverflow) * settings.xSpeedWeight / xSpeedMax)
        val normalizedYSpeed = max(0.0, (ySpeedMax - result.ySpeedOverflow) * settings.ySpeedWeight / ySpeedMax)
        val normalizedRotate = (rotateMax - result.rotateOverflow) * settings.rotateWeight / rotateMax
        val normalizedDistance = (distanceMax - result.distance) * settings.distanceWeight / distanceMax
        return normalizedXSpeed + normalizedYSpeed + normalizedRotate + normalizedDistance
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
        var i = -1
        do {
            i++
        } while (i < population.size && rnd > population[i].cumulativeScore)

        return population[i]
    }


    fun crossOver(parent1: Chromosome, parent2: Chromosome): Pair<Chromosome, Chromosome> {

        val child1Actions = mutableListOf<Action>()
        val child2Actions = mutableListOf<Action>()

        val weight = Random.nextDouble(1.0)
        for (i in 0 until settings.chromosomeSize) {


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
            if (Random.nextDouble(1.0) < settings.mutationProbability) {
                chromosome.actions[i] = generateAction()
            }
        }
    }

    fun nextGeneration() {

        var eliteSize = (settings.populationSize * settings.elitismPercent).toInt()
        if (eliteSize % 2 != 0) {
            eliteSize++
        }
        cumulativeScores()
        val children = mutableListOf<Chromosome>()
        while (children.size < settings.populationSize - eliteSize) {
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
    fun next(): AlgoResult {
        evaluation()
        val result = AlgoResult(this.settings.puzzle.surface, this.population.copyOf(), this.generationCount)
        nextGeneration()
        generationCount++
        return result
    }

//    fun findBestResult(): Chromosome {
//
//        var population = generateRandomPopulation()
//
//        lateinit var result: Chromosome
//        var generationCount = 0
//        while (generationCount < 100) {
//            evaluation(population)
//            population = nextGeneration(population)
//            generationCount++
//            var bestScore = 0.0
//            for (chromosome in population) {
//                if (chromosome.score > bestScore) {
//                    result = chromosome
//                    bestScore = chromosome.score
//                }
//            }
//            console.log("$generationCount -> $bestScore")
//        }
//        return result
//    }


}
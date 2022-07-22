import kotlin.math.roundToInt
import kotlin.random.Random

class Chromosome(var id: Int, var actions: Array<Action>) {
    var score = 0.0
    var cumulativeScore = 0.0
    var path = emptyList<Pair<Double, Double>>()
    var state: State? = null

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
    var puzzle: Puzzle,
)

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
//        var rotate = settings.puzzle.initialState.rotate
//        var power = settings.puzzle.initialState.power
//        var action = Action(rotate, power)
        return Chromosome(
            chromosomeIndex++,
            (0 until settings.chromosomeSize).map {
//                action = generateAction(action.rotate, action.power)
//                action
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
            it.score = state.play(it.actions, settings.puzzle.getSurfacePath())
            it.path = state.path
            it.state = state
        }
    }

    fun selection(): Array<Chromosome> {

        val eliteSize = (settings.populationSize * 0.1).toInt()
        val randomSize = (settings.populationSize * 0.4).toInt()
        val randomIndices = buildList {
            while (this.size < randomSize) {
                val random = (eliteSize until settings.populationSize).random()
                if (random !in this) add(random)
            }
        }
        val totalSize = eliteSize + randomSize
        population.sortByDescending { it.score }
        return Array(totalSize) {
            if (it < eliteSize) population[it]
            else population[randomIndices[it - eliteSize]]
        }

    }

    fun normalizeScores() {
        val sum = population.sumOf { it.score }
        for (chromosome in population) {
            chromosome.cumulativeScore = chromosome.score / sum
        }
    }

    fun cumulativeScores() {
        normalizeScores()
        population.sortBy { it.cumulativeScore }
        var cumul = 0.0
        for (chromosome in population) {
            chromosome.cumulativeScore += cumul
            cumul = chromosome.cumulativeScore
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

    fun crossover(parent1: Chromosome, parent2: Chromosome): Pair<Chromosome, Chromosome> {
        val child1Actions = parent1.actions.copyOf()
        val child2Actions = parent2.actions.copyOf()
        val realSize = parent1.path.size
        val index = Random.nextInt(realSize)
        for (i in index until settings.chromosomeSize) {
            child1Actions[i] = parent2.actions[i]
            child2Actions[i] = parent1.actions[i]
        }
        return Chromosome(chromosomeIndex++, child1Actions) to Chromosome(chromosomeIndex++, child2Actions)
    }

    fun weightCrossOver(parent1: Chromosome, parent2: Chromosome): Pair<Chromosome, Chromosome> {

        val child1Actions = mutableListOf<Action>()
        val child2Actions = mutableListOf<Action>()

        for (i in 0 until settings.chromosomeSize) {
            val weight = Random.nextDouble(1.0)

            val rotate1 = weight * parent1.actions[i].rotate + (1.0 - weight) * parent2.actions[i].rotate
            val rotate2 = (1.0 - weight) * parent1.actions[i].rotate + weight * parent2.actions[i].rotate
            val power1 = weight * parent1.actions[i].power + (1.0 - weight) * parent2.actions[i].power
            val power2 = (1.0 - weight) * parent1.actions[i].power + weight * parent2.actions[i].power
            child1Actions.add(Action(rotate1.roundToInt(), power1.roundToInt()))
            child2Actions.add(Action(rotate2.roundToInt(), power2.roundToInt()))
        }

        return Chromosome(chromosomeIndex++, child1Actions.toTypedArray()) to Chromosome(
            chromosomeIndex++,
            child2Actions.toTypedArray()
        )

    }


//    fun mutation(chromosome: Chromosome) {
//        for (i in chromosome.actions.indices) {
//            if (Random.nextDouble(1.0) < settings.mutationProbability) {
//                val (rotate, power) = if (i == 0) {
//                    settings.puzzle.initialState.rotate to settings.puzzle.initialState.power
//                } else {
//                    chromosome.actions[i - 1].rotate to chromosome.actions[i - 1].power
//                }
//                chromosome.actions[i] = generateAction(rotate, power)
//            }
//        }
//    }

    fun mutation(chromosome: Chromosome) {
        for (i in chromosome.actions.indices) {
            if (Random.nextDouble(1.0) < settings.mutationProbability) {
                chromosome.actions[i] = generateAction()
            }
        }
    }

    fun nextGeneration() {

//        val select = selection()
//        val children = mutableListOf<Chromosome>()
//
//        while (children.size < settings.populationSize / 2) {
//            val parent1 = select.random()
//            val parent2 = select.random()
//            val (child1, child2) = crossover(parent1, parent2).also { (a, b) -> mutation(a);mutation(b) }
//            children.add(child1)
//            children.add(child2)
//        }
//        population = select + children

        val eliteSize = 2
        cumulativeScores()
        val children = mutableListOf<Chromosome>()
        while (children.size < settings.populationSize - eliteSize) {
            val parent1 = wheelSelection()
            var parent2 : Chromosome? = null
            while (parent2 == null || parent2.id == parent1.id) {
                parent2 = wheelSelection()
            }
            val (child1, child2) = weightCrossOver(parent1, parent2).also { (a, b) -> mutation(a);mutation(b) }
            children.add(child1)
            children.add(child2)
        }
        population = population.takeLast(eliteSize).toTypedArray() + children.toTypedArray()
    }

    fun next(): AlgoResult {
        evaluation()
        val result = AlgoResult(this.settings.puzzle.surface, this.population, this.generationCount)
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
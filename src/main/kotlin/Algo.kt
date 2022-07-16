import kotlin.random.Random

class Chromosome(var actions: Array<Action>) {
    var score = 0.0
    var path = emptyList<Pair<Double,Double>>()
}

class AlgoOptions(
    var chromosomeSize: Int,
    var populationSize: Int,
    var puzzle: Puzzle,
    var onGeneration: (Array<Chromosome>) -> Unit,
    var onInit: (Puzzle) -> Unit
)

class GeneticAlgorithm(
    var options: AlgoOptions
) {

    var population = generateRandomPopulation()
    var generationCount = 0

    fun updateOptions(puzzle: Puzzle) {
        this.options.puzzle = puzzle
        generationCount = 0
        population = generateRandomPopulation()
        options.onInit(this.options.puzzle)
    }

    fun generateChromosome(): Chromosome {

        var rotate = options.puzzle.initialState.rotate
        var power = options.puzzle.initialState.power
        return Chromosome((0 until options.chromosomeSize).map {
            power = boundedValue(power + (-1..1).random(), 0, 4)
            rotate = boundedValue(rotate + (-15..15).random(), -90, 90)
            Action(rotate, power)
        }.toTypedArray())
    }

    fun generateRandomPopulation(): Array<Chromosome> {
        return Array(options.populationSize) { generateChromosome() }
    }

    fun evaluation() {
        population.forEach {
            val state = options.puzzle.initialState.copy()
            it.score = state.play(it.actions, options.puzzle.getSurfacePath())
            it.path = state.path
        }
    }

    fun selection() :Array<Chromosome> {

        val eliteSize = (options.populationSize * 0.3).toInt()
        val randomSize = (options.populationSize * 0.2).toInt()
        val randomIndices = buildList {
            while (this.size < randomSize) {
                val random = (eliteSize until options.populationSize).random()
                if (random !in this) add(random)
            }
        }
        val totalSize = eliteSize + randomSize
        population.sortByDescending { it.score }
        console.log(population)
        return Array(totalSize) {
            if (it < eliteSize) population[it]
            else population[randomIndices[it - eliteSize]]
        }

    }

    fun crossover(parent1: Chromosome, parent2: Chromosome): Chromosome {
        val childActions = parent1.actions.copyOf()
        for (i in options.chromosomeSize / 2 until options.chromosomeSize) {
            childActions[i] = parent2.actions[i]
        }
        return Chromosome(childActions)
    }

    fun mutation(chromosome: Chromosome) {
        val mutationProbability = 0.2
        for (i in chromosome.actions.indices) {
            if(Random.nextDouble(1.0) < mutationProbability){
                val (rotate, power) = if (i == 0) {
                    options.puzzle.initialState.rotate to options.puzzle.initialState.power
                } else {
                    chromosome.actions[i - 1].rotate to chromosome.actions[i - 1].power
                }
                chromosome.actions[i] = generateAction(rotate, power)
            }
        }
    }

    fun nextGeneration() {

        val select = selection()
        val children = mutableListOf<Chromosome>()

        while (children.size < options.populationSize / 2) {
            val parent1 = select.random()
            val parent2 = select.random()
            val child = crossover(parent1, parent2).apply(::mutation)
            children.add(child)
        }
        population = select + children
    }

    fun next() {
        evaluation()
        options.onGeneration(population)
        nextGeneration()
        generationCount++


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
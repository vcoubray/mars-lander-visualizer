import kotlin.random.Random

class Chromosome(var actions: Array<Action>) {
    var score = 0.0
    var path = emptyList<Pair<Double, Double>>()
}

class AlgoSettings(
    var chromosomeSize: Int,
    var populationSize: Int,
    var mutationProbability: Double,
    var puzzle: Puzzle,
)

class GeneticAlgorithm(
    var settings: AlgoSettings,
    var onChange: (surface: String, population: Array<Chromosome>, generation: Int) -> Unit
) {

    var population = generateRandomPopulation()
    var generationCount = 0

    init {
        onChange(this.settings.puzzle.surface, this.population, this.generationCount)
    }

    fun updateSettings(settings: AlgoSettings) {
        this.settings = settings
        reset()
    }

    fun reset() {
        generationCount = 0
        population = generateRandomPopulation()
        onChange(this.settings.puzzle.surface, this.population, this.generationCount)
    }

    fun generateChromosome(): Chromosome {
        var rotate = settings.puzzle.initialState.rotate
        var power = settings.puzzle.initialState.power
        var action = Action(rotate, power)
        return Chromosome((0 until settings.chromosomeSize).map {
            action = generateAction(action.rotate, action.power)
            action
//            power = boundedValue(power + (-1..1).random(), 0, 4)
//            rotate = boundedValue(rotate + (-15..15).random(), -90, 90)
//            Action(rotate, power)
        }.toTypedArray())
    }

    fun generateRandomPopulation(): Array<Chromosome> {
        return Array(settings.populationSize) { generateChromosome() }
    }

    fun evaluation() {
        population.forEach {
            val state = settings.puzzle.initialState.copy()
            it.score = state.play(it.actions, settings.puzzle.getSurfacePath())
            it.path = state.path
        }
    }

    fun selection(): Array<Chromosome> {

        val eliteSize = (settings.populationSize * 0.3).toInt()
        val randomSize = (settings.populationSize * 0.2).toInt()
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

    fun crossover(parent1: Chromosome, parent2: Chromosome): Chromosome {
        val childActions = parent1.actions.copyOf()
        val realSize = parent1.path.size
        val index= Random.nextInt(realSize)
        for (i in index until settings.chromosomeSize) {
            childActions[i] = parent2.actions[i]
        }
        return Chromosome(childActions)
    }

    fun mutation(chromosome: Chromosome) {
        for (i in chromosome.actions.indices) {
            if (Random.nextDouble(1.0) < settings.mutationProbability) {
                val (rotate, power) = if (i == 0) {
                    settings.puzzle.initialState.rotate to settings.puzzle.initialState.power
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

        while (children.size < settings.populationSize / 2) {
            val parent1 = select.random()
            val parent2 = select.random()
            val child = crossover(parent1, parent2).apply(::mutation)
            children.add(child)
        }
        population = select + children
    }

    fun next() {
        evaluation()
        onChange(this.settings.puzzle.surface, this.population, this.generationCount)
        console.log(population)
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
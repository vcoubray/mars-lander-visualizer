package algorithm

interface Engine<T : Chromosome> {
    fun simulate(chromosome: T)
    fun evaluate(chromosome: T)
    fun generateInitialPopulation(populationSize: Int, chromosomeSize: Int): Array<T>
    fun generateChildrenPopulation(populationSize: Int, chromosomeSize: Int): Array<T>
    fun crossOver(parent1: T, parent2: T, children1: T, children2: T)
    fun mutate(chromosome: T, mutationProbability: Double)
}

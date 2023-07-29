
fun SimulationResult.toSummary() = SimulationSummary(
    id = this.id,
    simulationSettings = this.simulationSettings,
    status = this.status,
    duration = this.duration,
    bestScore = this.bestScore,
    generationCount = this.generations.size
)

fun GenerationResult.toSummary() = GenerationSummary(
    populationSize = this.population.size,
    best = this.best,
    mean = this.mean
)
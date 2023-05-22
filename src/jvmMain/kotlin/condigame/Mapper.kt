package condigame


import MarsChromosomeResult

fun MarsChromosome.toResult(id: Int) = MarsChromosomeResult(
    id,
    actions = actions.toList(),
    path = path,
    state = state,
    score = score,
    normalizedScore = normalizedScore,
    cumulativeScore = cumulativeScore,
    fitnessResult = fitnessResult
)

package fr.vco.genetic.algorithm.visualizer.server.services

import fr.vco.genetic.algorithm.visualizer.BenchmarkSettings
import fr.vco.genetic.algorithm.visualizer.SimulationSettings
import fr.vco.genetic.algorithm.visualizer.persistence.BenchmarkRepository
import fr.vco.genetic.algorithm.visualizer.puzzle.PuzzleRegistry
import fr.vco.genetic.algorithm.visualizer.server.exceptions.AlreadyRunningException
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class BenchmarkService(
    private val registry: PuzzleRegistry,
    private val statusService: ServerStatusService,
    private val repo: BenchmarkRepository,
    private val puzzleService: PuzzleService,
) {
    fun start(settings: BenchmarkSettings<*>): Int {
        if (!statusService.isRunning.compareAndSet(false, true)) throw AlreadyRunningException()
        val benchmarkId = repo.insertPending(settings)

        thread {
            try {
                for (puzzleId in settings.puzzlesId) {
                    @Suppress("UNCHECKED_CAST")
                    val runSettings = SimulationSettings(
                        settings.globalSettings,
                        settings.engineSettings.withPuzzleId(puzzleId),
                    ) as SimulationSettings<*>
                    for (runIdx in 0 until settings.runCount) {
                        val runId = repo.insertRun(benchmarkId, puzzleId, runIdx, runSettings)
                        val module = registry.findBySettings(runSettings)
                        var bestScore = 0.0
                        var genCount = 0
                        val dur = measureTimeMillis {
                            module.runSimulation(runSettings) { gen ->
                                bestScore = gen.best
                                genCount++
                            }
                        }
                        repo.completeRun(runId, dur, bestScore, genCount)
                    }
                }
                repo.complete(benchmarkId)
                println("Benchmark '$benchmarkId' complete")
            } finally {
                statusService.isRunning.set(false)
            }
        }
        return benchmarkId
    }

    fun getAll() = repo.findAll { puzzleService.getPuzzle(it) }
    fun get(id: Int) = repo.find(id) { puzzleService.getPuzzle(it) }
    fun delete(id: Int): Boolean = repo.delete(id)
}

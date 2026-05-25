package fr.vco.genetic.algorithm.visualizer.persistence

import fr.vco.genetic.algorithm.visualizer.*
import fr.vco.genetic.algorithm.visualizer.server.exceptions.ConflictException
import kotlinx.serialization.json.Json
import org.ktorm.database.Database
import org.ktorm.dsl.*

class BenchmarkRepository(private val db: Database, private val json: Json) {

    private val benchmarkSettingsSerializer = BenchmarkSettings.serializer(EngineSettings.serializer())
    private val simulationSettingsSerializer = SimulationSettings.serializer(EngineSettings.serializer())

    @Suppress("UNCHECKED_CAST")
    fun insertPending(settings: BenchmarkSettings<*>): Int {
        return db.insertAndGenerateKey(Benchmarks) {
            set(it.settingsJson, json.encodeToString(benchmarkSettingsSerializer, settings as BenchmarkSettings<EngineSettings>))
            set(it.status, SimulationStatus.PENDING.name)
            set(it.createdAt, System.currentTimeMillis())
        } as Int
    }

    @Suppress("UNCHECKED_CAST")
    fun insertRun(benchmarkId: Int, puzzleId: Int, runIdx: Int, settings: SimulationSettings<*>): Int {
        return db.insertAndGenerateKey(BenchmarkRuns) {
            set(it.benchmarkId, benchmarkId)
            set(it.puzzleId, puzzleId)
            set(it.runIdx, runIdx)
            set(it.status, SimulationStatus.PENDING.name)
            set(it.durationMs, 0L)
            set(it.bestScore, 0.0)
            set(it.generationCount, 0)
            set(it.settingsJson, json.encodeToString(simulationSettingsSerializer, settings as SimulationSettings<EngineSettings>))
        } as Int
    }

    fun completeRun(runId: Int, durationMs: Long, bestScore: Double, generationCount: Int) {
        db.update(BenchmarkRuns) {
            set(it.status, SimulationStatus.COMPLETE.name)
            set(it.durationMs, durationMs)
            set(it.bestScore, bestScore)
            set(it.generationCount, generationCount)
            where { it.id eq runId }
        }
    }

    fun complete(id: Int) {
        db.update(Benchmarks) {
            set(it.status, SimulationStatus.COMPLETE.name)
            where { it.id eq id }
        }
    }

    fun findAll(puzzleLookup: (Int) -> Puzzle?): List<BenchmarkResult> {
        val runs = loadAllRuns(puzzleLookup)
        return db.from(Benchmarks)
            .select()
            .orderBy(Benchmarks.id.asc())
            .map { row ->
                val id = row[Benchmarks.id]!!
                BenchmarkResult(
                    id = id,
                    settings = json.decodeFromString(benchmarkSettingsSerializer, row[Benchmarks.settingsJson]!!),
                    status = SimulationStatus.valueOf(row[Benchmarks.status]!!),
                    runs = runs[id] ?: emptyList(),
                )
            }
    }

    fun find(id: Int, puzzleLookup: (Int) -> Puzzle?): BenchmarkResult? {
        return db.from(Benchmarks)
            .select()
            .where { Benchmarks.id eq id }
            .map { row ->
                BenchmarkResult(
                    id = row[Benchmarks.id]!!,
                    settings = json.decodeFromString(benchmarkSettingsSerializer, row[Benchmarks.settingsJson]!!),
                    status = SimulationStatus.valueOf(row[Benchmarks.status]!!),
                    runs = loadRunsForBenchmark(id, puzzleLookup),
                )
            }
            .firstOrNull()
    }

    fun isRunning(id: Int): Boolean {
        return db.from(Benchmarks)
            .select(Benchmarks.status)
            .where { Benchmarks.id eq id }
            .map { it[Benchmarks.status] }
            .firstOrNull() == SimulationStatus.PENDING.name
    }

    fun delete(id: Int): Boolean {
        if (isRunning(id)) throw ConflictException("Benchmark $id is still running")
        val deleted = db.delete(Benchmarks) { it.id eq id }
        return deleted > 0
    }

    fun deleteAll() {
        db.delete(Benchmarks) { it.id.isNotNull() }
    }

    private fun loadAllRuns(puzzleLookup: (Int) -> Puzzle?): Map<Int, List<BenchmarkRun>> {
        val rows = db.from(BenchmarkRuns)
            .select()
            .orderBy(BenchmarkRuns.benchmarkId.asc(), BenchmarkRuns.puzzleId.asc(), BenchmarkRuns.runIdx.asc())
            .map { it }

        return rows
            .groupBy { it[BenchmarkRuns.benchmarkId]!! }
            .mapValues { (_, benchRows) -> groupRunsByPuzzle(benchRows, puzzleLookup) }
    }

    private fun loadRunsForBenchmark(benchmarkId: Int, puzzleLookup: (Int) -> Puzzle?): List<BenchmarkRun> {
        val rows = db.from(BenchmarkRuns)
            .select()
            .where { BenchmarkRuns.benchmarkId eq benchmarkId }
            .orderBy(BenchmarkRuns.puzzleId.asc(), BenchmarkRuns.runIdx.asc())
            .map { it }

        return groupRunsByPuzzle(rows, puzzleLookup)
    }

    private fun groupRunsByPuzzle(
        rows: List<QueryRowSet>,
        puzzleLookup: (Int) -> Puzzle?,
    ): List<BenchmarkRun> {
        return rows
            .groupBy { it[BenchmarkRuns.puzzleId]!! }
            .mapNotNull { (puzzleId, puzzleRows) ->
                val puzzle = puzzleLookup(puzzleId) ?: return@mapNotNull null
                val summaries = puzzleRows.map { row ->
                    SimulationSummary(
                        id = row[BenchmarkRuns.id]!!,
                        simulationSettings = json.decodeFromString(simulationSettingsSerializer, row[BenchmarkRuns.settingsJson]!!),
                        status = SimulationStatus.valueOf(row[BenchmarkRuns.status]!!),
                        duration = row[BenchmarkRuns.durationMs]!!,
                        bestScore = row[BenchmarkRuns.bestScore]!!,
                        generationCount = row[BenchmarkRuns.generationCount]!!,
                    )
                }
                val allComplete = summaries.all { it.status == SimulationStatus.COMPLETE }
                BenchmarkRun(
                    puzzle = puzzle,
                    status = if (allComplete) SimulationStatus.COMPLETE else SimulationStatus.PENDING,
                    simulationsSummaries = summaries,
                )
            }
    }
}

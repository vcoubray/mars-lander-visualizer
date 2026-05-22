package fr.vco.genetic.algorithm.visualizer.persistence

import org.ktorm.schema.Table
import org.ktorm.schema.double
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.varchar

object Simulations : Table<Nothing>("simulation") {
    val id = int("id").primaryKey()
    val settingsJson = varchar("settings_json")
    val status = varchar("status")
    val durationMs = long("duration_ms")
    val bestScore = double("best_score")
    val generationCount = int("generation_count")
    val createdAt = long("created_at")
}

object Generations : Table<Nothing>("generation") {
    val id = int("id").primaryKey()
    val simulationId = int("simulation_id")
    val generationIdx = int("generation_idx")
    val bestScore = double("best_score")
    val meanScore = double("mean_score")
    val populationSize = int("population_size")
}

object Chromosomes : Table<Nothing>("chromosome") {
    val id = int("id").primaryKey()
    val generationId = int("generation_id")
    val chromosomeIdx = int("chromosome_idx")
    val score = double("score")
    val normalizedScore = double("normalized_score")
    val cumulativeScore = double("cumulative_score")
    val payloadJson = varchar("payload_json")
}

object Benchmarks : Table<Nothing>("benchmark") {
    val id = int("id").primaryKey()
    val settingsJson = varchar("settings_json")
    val status = varchar("status")
    val createdAt = long("created_at")
}

object BenchmarkRuns : Table<Nothing>("benchmark_run") {
    val id = int("id").primaryKey()
    val benchmarkId = int("benchmark_id")
    val puzzleId = int("puzzle_id")
    val runIdx = int("run_idx")
    val status = varchar("status")
    val durationMs = long("duration_ms")
    val bestScore = double("best_score")
    val generationCount = int("generation_count")
    val settingsJson = varchar("settings_json")
}

package fr.vco.genetic.algorithm.visualizer.persistence

import fr.vco.genetic.algorithm.visualizer.Action
import fr.vco.genetic.algorithm.visualizer.FitnessResult
import fr.vco.genetic.algorithm.visualizer.GenerationResult
import fr.vco.genetic.algorithm.visualizer.GenerationSummary
import fr.vco.genetic.algorithm.visualizer.MarsChromosomeResult
import fr.vco.genetic.algorithm.visualizer.SimulationSettings
import fr.vco.genetic.algorithm.visualizer.SimulationStatus
import fr.vco.genetic.algorithm.visualizer.SimulationSummary
import fr.vco.genetic.algorithm.visualizer.State
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.asc
import org.ktorm.dsl.batchInsert
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.dsl.isNotNull
import org.ktorm.dsl.map
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.update
import org.ktorm.dsl.where

@Serializable
private data class ChromosomePayload(
    val actions: List<Action>,
    val path: List<Pair<Double, Double>>,
    val state: State,
    val fitnessResult: FitnessResult?,
)

class SimulationRepository(private val db: Database, private val json: Json) {

    fun insertPending(settings: SimulationSettings<*>): Int {
        return db.insertAndGenerateKey(Simulations) {
            set(it.settingsJson, json.encodeToString(settings))
            set(it.status, SimulationStatus.PENDING.name)
            set(it.durationMs, 0L)
            set(it.bestScore, 0.0)
            set(it.generationCount, 0)
            set(it.createdAt, System.currentTimeMillis())
        } as Int
    }

    fun complete(id: Int, durationMs: Long, bestScore: Double, generationCount: Int) {
        db.update(Simulations) {
            set(it.status, SimulationStatus.COMPLETE.name)
            set(it.durationMs, durationMs)
            set(it.bestScore, bestScore)
            set(it.generationCount, generationCount)
            where { it.id eq id }
        }
    }

    fun insertGeneration(simulationId: Int, idx: Int, gen: GenerationResult): Int {
        return db.insertAndGenerateKey(Generations) {
            set(it.simulationId, simulationId)
            set(it.generationIdx, idx)
            set(it.bestScore, gen.best)
            set(it.meanScore, gen.mean)
            set(it.populationSize, gen.population.size)
        } as Int
    }

    fun insertChromosomes(generationId: Int, chromosomes: List<MarsChromosomeResult>) {
        if (chromosomes.isEmpty()) return
        db.batchInsert(Chromosomes) {
            for (c in chromosomes) {
                item {
                    set(it.generationId, generationId)
                    set(it.chromosomeIdx, c.id)
                    set(it.score, c.score)
                    set(it.normalizedScore, c.normalizedScore)
                    set(it.cumulativeScore, c.cumulativeScore)
                    set(it.payloadJson, json.encodeToString(ChromosomePayload(c.actions, c.path, c.state, c.fitnessResult)))
                }
            }
        }
    }

    fun findSummaries(): List<SimulationSummary> {
        return db.from(Simulations)
            .select()
            .orderBy(Simulations.id.asc())
            .map { row ->
                SimulationSummary(
                    id = row[Simulations.id]!!,
                    simulationSettings = json.decodeFromString(row[Simulations.settingsJson]!!),
                    status = SimulationStatus.valueOf(row[Simulations.status]!!),
                    duration = row[Simulations.durationMs]!!,
                    bestScore = row[Simulations.bestScore]!!,
                    generationCount = row[Simulations.generationCount]!!,
                )
            }
    }

    fun findSummary(id: Int): SimulationSummary? {
        return db.from(Simulations)
            .select()
            .where { Simulations.id eq id }
            .map { row ->
                SimulationSummary(
                    id = row[Simulations.id]!!,
                    simulationSettings = json.decodeFromString(row[Simulations.settingsJson]!!),
                    status = SimulationStatus.valueOf(row[Simulations.status]!!),
                    duration = row[Simulations.durationMs]!!,
                    bestScore = row[Simulations.bestScore]!!,
                    generationCount = row[Simulations.generationCount]!!,
                )
            }
            .firstOrNull()
    }

    fun findGenerationSummaries(simulationId: Int): List<GenerationSummary>? {
        if (findSummary(simulationId) == null) return null
        return db.from(Generations)
            .select()
            .where { Generations.simulationId eq simulationId }
            .orderBy(Generations.generationIdx.asc())
            .map { row ->
                GenerationSummary(
                    populationSize = row[Generations.populationSize]!!,
                    best = row[Generations.bestScore]!!,
                    mean = row[Generations.meanScore]!!,
                )
            }
    }

    fun findGeneration(simulationId: Int, generationIdx: Int): GenerationResult? {
        val genRow = db.from(Generations)
            .select()
            .where { (Generations.simulationId eq simulationId) and (Generations.generationIdx eq generationIdx) }
            .map { it }
            .firstOrNull() ?: return null

        val generationId = genRow[Generations.id]!!

        val chromosomes = db.from(Chromosomes)
            .select()
            .where { Chromosomes.generationId eq generationId }
            .orderBy(Chromosomes.chromosomeIdx.asc())
            .map { row ->
                val payload = json.decodeFromString<ChromosomePayload>(row[Chromosomes.payloadJson]!!)
                MarsChromosomeResult(
                    id = row[Chromosomes.chromosomeIdx]!!,
                    actions = payload.actions,
                    path = payload.path,
                    state = payload.state,
                    score = row[Chromosomes.score]!!,
                    normalizedScore = row[Chromosomes.normalizedScore]!!,
                    cumulativeScore = row[Chromosomes.cumulativeScore]!!,
                    fitnessResult = payload.fitnessResult,
                )
            }

        return GenerationResult(chromosomes)
    }

    fun delete(id: Int): Boolean {
        val deleted = db.delete(Simulations) { it.id eq id }
        return deleted > 0
    }

    fun deleteAll() {
        db.delete(Simulations) { it.id.isNotNull() }
    }
}

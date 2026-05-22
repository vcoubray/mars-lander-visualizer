package fr.vco.genetic.algorithm.visualizer.persistence

import fr.vco.genetic.algorithm.visualizer.Action
import fr.vco.genetic.algorithm.visualizer.CrossingEnum
import fr.vco.genetic.algorithm.visualizer.CrossoverType
import fr.vco.genetic.algorithm.visualizer.FitnessResult
import fr.vco.genetic.algorithm.visualizer.GenerationResult
import fr.vco.genetic.algorithm.visualizer.GlobalSettings
import fr.vco.genetic.algorithm.visualizer.LimitType
import fr.vco.genetic.algorithm.visualizer.MarsChromosomeResult
import fr.vco.genetic.algorithm.visualizer.MarsEngineSettings
import fr.vco.genetic.algorithm.visualizer.MutationType
import fr.vco.genetic.algorithm.visualizer.SelectionType
import fr.vco.genetic.algorithm.visualizer.SimulationSettings
import fr.vco.genetic.algorithm.visualizer.SimulationStatus
import fr.vco.genetic.algorithm.visualizer.State
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.serialization.json.Json

private fun testSettings() = SimulationSettings(
    globalSettings = GlobalSettings(
        limitType = LimitType.TIME,
        limitValue = 2000,
        chromosomeSize = 10,
        populationSize = 5,
        mutationProbability = 0.1,
        elitismPercent = 0.2,
        selectionType = SelectionType.RANDOM,
        crossoverType = CrossoverType.BLEND,
        mutationType = MutationType.PER_GENE,
    ),
    engineSettings = MarsEngineSettings(
        puzzleId = 1,
        speedMax = 100.0,
        xSpeedWeight = 50.0,
        ySpeedWeight = 50.0,
        rotateWeight = 10.0,
        distanceWeight = 250.0,
        crashSpeedWeight = 0.9,
    ),
)

private fun testChromosome(idx: Int) = MarsChromosomeResult(
    id = idx,
    actions = listOf(Action(0, 2), Action(5, 3)),
    path = listOf(2000.0 to 2000.0, 2010.0 to 1990.0),
    state = State(x = 2010.0, y = 1990.0),
    score = idx * 10.0,
    normalizedScore = 0.5,
    cumulativeScore = 0.5,
    fitnessResult = FitnessResult(100.0, 0.0, 0.0, 0, CrossingEnum.NOPE),
)

private fun testGeneration(size: Int = 3) = GenerationResult(
    population = (0 until size).map { testChromosome(it) }
)

class SimulationRepositoryTest : FunSpec({
    val json = Json { ignoreUnknownKeys = true }
    lateinit var repo: SimulationRepository

    beforeEach {
        repo = SimulationRepository(buildInMemoryDatabase(), json)
    }

    test("insert pending and find summary") {
        val settings = testSettings()
        val id = repo.insertPending(settings)
        val summary = repo.findSummary(id)
        summary.shouldNotBeNull()
        summary.id shouldBe id
        summary.status shouldBe SimulationStatus.PENDING
        summary.bestScore shouldBe 0.0
        summary.generationCount shouldBe 0
        summary.simulationSettings shouldBe settings
    }

    test("complete updates summary fields") {
        val id = repo.insertPending(testSettings())
        repo.complete(id, 1500L, 42.0, 10)
        val summary = repo.findSummary(id)!!
        summary.status shouldBe SimulationStatus.COMPLETE
        summary.duration shouldBe 1500L
        summary.bestScore shouldBe 42.0
        summary.generationCount shouldBe 10
    }

    test("insert and retrieve generation with chromosomes") {
        val id = repo.insertPending(testSettings())
        val gen = testGeneration(3)
        val genId = repo.insertGeneration(id, 0, gen)
        repo.insertChromosomes(genId, gen.population)

        val retrieved = repo.findGeneration(id, 0)
        retrieved.shouldNotBeNull()
        retrieved.population shouldHaveSize 3
        val c0 = retrieved.population[0]
        c0.score shouldBe 0.0
        c0.actions shouldBe gen.population[0].actions
        c0.path shouldBe gen.population[0].path
        c0.fitnessResult shouldNotBe null
    }

    test("findGenerationSummaries returns summaries in order") {
        val id = repo.insertPending(testSettings())
        repeat(5) { idx ->
            val gen = testGeneration()
            val genId = repo.insertGeneration(id, idx, gen)
            repo.insertChromosomes(genId, gen.population)
        }
        val summaries = repo.findGenerationSummaries(id)
        summaries.shouldNotBeNull()
        summaries shouldHaveSize 5
    }

    test("findGenerationSummaries returns null for unknown simulation") {
        repo.findGenerationSummaries(999) shouldBe null
    }

    test("delete removes simulation and cascades to generations and chromosomes") {
        val id = repo.insertPending(testSettings())
        val gen = testGeneration()
        val genId = repo.insertGeneration(id, 0, gen)
        repo.insertChromosomes(genId, gen.population)

        repo.delete(id).shouldBeTrue()
        repo.findSummary(id) shouldBe null
        repo.findGeneration(id, 0) shouldBe null
    }

    test("delete returns false for unknown id") {
        repo.delete(999) shouldBe false
    }

    test("deleteAll clears all simulations") {
        repo.insertPending(testSettings())
        repo.insertPending(testSettings())
        repo.deleteAll()
        repo.findSummaries() shouldHaveSize 0
    }

    test("findSummaries returns all simulations in insertion order") {
        repeat(3) { repo.insertPending(testSettings()) }
        repo.findSummaries() shouldHaveSize 3
    }
})

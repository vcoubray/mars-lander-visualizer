package fr.vco.genetic.algorithm.visualizer.persistence

import fr.vco.genetic.algorithm.visualizer.BenchmarkSettings
import fr.vco.genetic.algorithm.visualizer.CrossoverType
import fr.vco.genetic.algorithm.visualizer.GlobalSettings
import fr.vco.genetic.algorithm.visualizer.LimitType
import fr.vco.genetic.algorithm.visualizer.MarsEngineSettings
import fr.vco.genetic.algorithm.visualizer.MutationType
import fr.vco.genetic.algorithm.visualizer.Puzzle
import fr.vco.genetic.algorithm.visualizer.SelectionType
import fr.vco.genetic.algorithm.visualizer.SimulationSettings
import fr.vco.genetic.algorithm.visualizer.SimulationStatus
import fr.vco.genetic.algorithm.visualizer.State
import fr.vco.genetic.algorithm.visualizer.server.exceptions.ConflictException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

private val globalSettings = GlobalSettings(
    limitType = LimitType.TIME,
    limitValue = 2000,
    chromosomeSize = 10,
    populationSize = 5,
    mutationProbability = 0.1,
    elitismPercent = 0.2,
    selectionType = SelectionType.RANDOM,
    crossoverType = CrossoverType.BLEND,
    mutationType = MutationType.PER_GENE,
)

private val marsSettings = MarsEngineSettings(
    puzzleId = 1,
    speedMax = 100.0,
    xSpeedWeight = 50.0,
    ySpeedWeight = 50.0,
    rotateWeight = 10.0,
    distanceWeight = 250.0,
    crashSpeedWeight = 0.9,
)

private val benchmarkSettings = BenchmarkSettings(
    puzzlesId = listOf(1, 2),
    runCount = 2,
    globalSettings = globalSettings,
    engineSettings = marsSettings,
)

private fun testPuzzle(id: Int) = Puzzle(
    id = id,
    title = "Puzzle $id",
    surface = "0 0 7000 0",
    initialState = State(x = 2500.0, y = 2700.0, fuel = 500),
)

private val puzzleLookup: (Int) -> Puzzle? = { id -> testPuzzle(id) }

private fun runSettings(puzzleId: Int) = SimulationSettings(
    globalSettings = globalSettings,
    engineSettings = marsSettings.copy(puzzleId = puzzleId),
)

class BenchmarkRepositoryTest : FunSpec({
    val json = Json { ignoreUnknownKeys = true }
    lateinit var repo: BenchmarkRepository

    beforeEach {
        repo = BenchmarkRepository(buildInMemoryDatabase(), json)
    }

    test("insert pending and find it") {
        val id = repo.insertPending(benchmarkSettings)
        val result = repo.find(id, puzzleLookup)
        result.shouldNotBeNull()
        result.id shouldBe id
        result.status shouldBe SimulationStatus.PENDING
        result.runs shouldHaveSize 0
    }

    test("full lifecycle: pending -> runs -> complete") {
        val benchId = repo.insertPending(benchmarkSettings)

        val run1 = repo.insertRun(benchId, 1, 0, runSettings(1))
        val run2 = repo.insertRun(benchId, 1, 1, runSettings(1))
        val run3 = repo.insertRun(benchId, 2, 0, runSettings(2))
        val run4 = repo.insertRun(benchId, 2, 1, runSettings(2))

        repo.completeRun(run1, 1200L, 85.0, 50)
        repo.completeRun(run2, 1100L, 90.0, 48)
        repo.completeRun(run3, 1300L, 75.0, 52)
        repo.completeRun(run4, 1050L, 80.0, 45)
        repo.complete(benchId)

        val result = repo.find(benchId, puzzleLookup)!!
        result.status shouldBe SimulationStatus.COMPLETE
        result.runs shouldHaveSize 2

        val runsForPuzzle1 = result.runs.first { it.puzzle.id == 1 }
        runsForPuzzle1.simulationsSummaries shouldHaveSize 2
        runsForPuzzle1.status shouldBe SimulationStatus.COMPLETE
    }

    test("findAll includes all benchmarks") {
        repo.insertPending(benchmarkSettings)
        repo.insertPending(benchmarkSettings)
        repo.findAll(puzzleLookup) shouldHaveSize 2
    }

    test("delete a completed benchmark") {
        val id = repo.insertPending(benchmarkSettings)
        repo.complete(id)
        repo.delete(id).shouldBeTrue()
        repo.find(id, puzzleLookup) shouldBe null
    }

    test("delete returns false for unknown id") {
        repo.complete(repo.insertPending(benchmarkSettings))
        repo.delete(999) shouldBe false
    }

    test("delete a pending benchmark throws ConflictException") {
        val id = repo.insertPending(benchmarkSettings)
        shouldThrow<ConflictException> {
            repo.delete(id)
        }
    }

    test("deleteAll clears all benchmarks") {
        val id1 = repo.insertPending(benchmarkSettings)
        val id2 = repo.insertPending(benchmarkSettings)
        repo.complete(id1)
        repo.complete(id2)
        repo.deleteAll()
        repo.findAll(puzzleLookup) shouldHaveSize 0
    }
})

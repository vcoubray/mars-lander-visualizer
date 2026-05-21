package fr.vco.genetic.algorithm.visualizer.server.services

import fr.vco.genetic.algorithm.visualizer.BenchmarkSettings
import fr.vco.genetic.algorithm.visualizer.BenchmarkResult
import fr.vco.genetic.algorithm.visualizer.server.exceptions.AlreadyRunningException
import kotlin.concurrent.thread

class BenchmarkService(
    private val statusService: ServerStatusService,
){

    private val benchmarks: MutableMap<Int, BenchmarkResult> = mutableMapOf()
    private var lastId = 0

    fun start(benchmarkSettings: BenchmarkSettings<*>): Int {
        if (!statusService.isRunning.compareAndSet(false, true)) {
            throw AlreadyRunningException()
        }

        val id = lastId++
        benchmarks[id] = BenchmarkResult(id, benchmarkSettings)

        thread {
            Thread.sleep(1000)
            println("Benchmark '$id' complete")
            statusService.isRunning.set(false)
        }
        return id

    }

    fun getResults() = this.benchmarks.values
}
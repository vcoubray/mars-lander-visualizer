package services

import BenchmarkSettings
import codingame.BenchmarkResult
import exceptions.AlreadyRunningException
import kotlin.concurrent.thread

class BenchmarkService(
    private val algorithmFactory : AlgorithmFactory,
    private val statusService: ServerStatusService
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
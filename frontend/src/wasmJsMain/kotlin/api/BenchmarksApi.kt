package api

import apiPrefix
import fr.vco.genetic.algorithm.visualizer.BenchmarkResult
import fr.vco.genetic.algorithm.visualizer.BenchmarkSettings
import fr.vco.genetic.algorithm.visualizer.MarsEngineSettings
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class BenchmarksApi(
    private val client: HttpClient,
    private val base: String,
) {
    private val prefix = "$base$apiPrefix/benchmarks"

    suspend fun getAll(): List<BenchmarkResult> = client.get(prefix).body()
    suspend fun get(id: Int): BenchmarkResult = client.get("$prefix/$id").body()
    suspend fun create(settings: BenchmarkSettings<MarsEngineSettings>): Int =
        client.post(prefix) {
            contentType(ContentType.Application.Json)
            setBody(settings)
        }.body()

    suspend fun delete(id: Int) {
        client.delete("$prefix/$id")
    }
}

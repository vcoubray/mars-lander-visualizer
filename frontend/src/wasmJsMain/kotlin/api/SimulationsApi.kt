package api

import apiPrefix
import fr.vco.genetic.algorithm.visualizer.GenerationResult
import fr.vco.genetic.algorithm.visualizer.GenerationSummary
import fr.vco.genetic.algorithm.visualizer.MarsSettings
import fr.vco.genetic.algorithm.visualizer.SimulationResult
import fr.vco.genetic.algorithm.visualizer.SimulationSummary
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class SimulationsApi(
    private val client: HttpClient,
    private val base: String
) {

    private val prefix = "$base$apiPrefix/simulations"

    suspend fun getAll(): List<SimulationSummary> =
        client.get(prefix).body()

    suspend fun get(id: Int): SimulationResult =
        client.get("$prefix/$id").body()

    suspend fun create(settings: MarsSettings): Int =
        client.post(prefix) {
            contentType(ContentType.Application.Json)
            setBody(settings)
        }.body()

    suspend fun delete(id: Int) {
        client.delete("$prefix/$id")
    }

    suspend fun getGenerations(id: Int): List<GenerationSummary> =
        client.get("$prefix/$id/generations").body()

    suspend fun getGeneration(id: Int, generationId: Int): GenerationResult =
        client.get("$prefix/$id/generation/$generationId").body()

}

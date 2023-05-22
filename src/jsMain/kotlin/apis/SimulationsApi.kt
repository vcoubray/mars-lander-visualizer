package apis

import AlgoSettings
import GenerationResult
import GenerationSummary
import SimulationSummary
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun fetchSimulations(): List<SimulationSummary> {
    return httpJsonClient.get("${endpoint}/simulations").body()
}

suspend fun fetchSimulation(simulationId: Int) : SimulationSummary {
    return httpJsonClient.get("${endpoint}/simulations/$simulationId").body()
}

suspend fun startSimulations(settings: AlgoSettings): Int {
    return httpJsonClient.post("${endpoint}/simulations") {
        contentType(ContentType.Application.Json)
        accept(ContentType.Text.Plain)
        setBody(settings)
    }.bodyAsText().toInt()
}

suspend fun deleteSimulation(simulationId: Int) {
    return httpJsonClient.delete("${endpoint}/simulations/${simulationId}").body()
}

suspend fun fetchGenerations(simulationId: Int) : List<GenerationSummary> {
    return httpJsonClient.get("${endpoint}/simulations/${simulationId}/generations").body()
}

suspend fun fetchGeneration(simulationId: Int, generationId: Int) : GenerationResult {
    return httpJsonClient.get("${endpoint}/simulations/${simulationId}/generations/${generationId}").body()
}

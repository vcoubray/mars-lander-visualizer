package apis

import AlgoSettings
import Generation
import GenerationSummary
import SimulationResult
import SimulationSummary
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun fetchSimulations(): List<SimulationSummary> {
    return jsonClient.get("${endpoint}/simulations").body()
}

suspend fun fetchSimulation(simulationId: Int) : SimulationSummary {
    return jsonClient.get("${endpoint}/simulations/$simulationId").body()
}

suspend fun startSimulations(settings: AlgoSettings): Int {
    return jsonClient.post("${endpoint}/simulations") {
        contentType(ContentType.Application.Json)
        accept(ContentType.Text.Plain)
        setBody(settings)
    }.bodyAsText().toInt()
}

suspend fun fetchGenerations(simulationId: Int) : List<GenerationSummary> {
    return jsonClient.get("${endpoint}/simulations/${simulationId}/generations").body()
}

suspend fun fetchGeneration(simulationId: Int, generationId: Int) : Generation {
    return jsonClient.get("${endpoint}/simulations/${simulationId}/generations/${generationId}").body()
}

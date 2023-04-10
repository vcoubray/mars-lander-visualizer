package apis

import AlgoSettings
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

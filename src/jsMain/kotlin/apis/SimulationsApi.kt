package apis

import AlgoSettings
import SimulationSummary
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun fetchSimulations(): List<SimulationSummary> {
    return jsonClient.get("${endpoint}/simulations").body()
}

suspend fun startSimulations(settings: AlgoSettings): Int {
    jsonClient.post("${endpoint}/simulations") {
        contentType(ContentType.Application.Json)
        setBody(settings)
    }
    return 0
}
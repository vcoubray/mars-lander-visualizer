package apis

import AlgoSettings
import GenerationResult
import RunStats
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*


suspend fun resetAlgo(settings: AlgoSettings): GenerationResult {
    return jsonClient.post("$endpoint/algo/reset") {
        contentType(ContentType.Application.Json)
        setBody(settings)
    }.body()
}

suspend fun algoNext(): GenerationResult {
    return jsonClient.get("$endpoint/algo/next") {
        contentType(ContentType.Application.Json)
    }.body()
}

suspend fun algoPlay(settings: AlgoSettings): RunStats{
    return jsonClient.post("$endpoint/algo/play") {
        contentType(ContentType.Application.Json)
        setBody(settings)
    }.body()
}
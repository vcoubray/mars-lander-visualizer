package apis

import AlgoSettings
import PopulationResult
import Puzzle
import PuzzleResult
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.browser.window

val endpoint = window.location.origin

val jsonClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

suspend fun getPuzzles(): List<Puzzle> {
    return jsonClient.get(endpoint + Puzzle.path).body()
}

suspend fun resetAlgo(settings: AlgoSettings): PopulationResult {
    return jsonClient.post("$endpoint/algo/reset") {
        contentType(ContentType.Application.Json)
        setBody(settings)
    }.body()
}

suspend fun algoNext(): PopulationResult {
    return jsonClient.get("$endpoint/algo/next") {
        contentType(ContentType.Application.Json)
    }.body()
}

suspend fun algoPlay(settings: AlgoSettings): PuzzleResult{
    return jsonClient.post("$endpoint/algo/play") {
        contentType(ContentType.Application.Json)
        setBody(settings)
    }.body()
}
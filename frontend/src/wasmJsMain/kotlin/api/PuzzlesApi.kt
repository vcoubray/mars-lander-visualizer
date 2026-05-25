package api

import apiPrefix
import fr.vco.genetic.algorithm.visualizer.Puzzle
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class PuzzlesApi(
    private val client: HttpClient,
    private val base: String
) {
    private val prefix = "$base$apiPrefix${Puzzle.path}"

    suspend fun getAll():List<Puzzle> = client.get(prefix).body()
    suspend fun get(id: Int): Puzzle = client.get("$prefix/$id").body()

}
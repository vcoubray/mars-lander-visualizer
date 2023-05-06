package apis

import Puzzle
import io.ktor.client.call.*
import io.ktor.client.request.*

suspend fun fetchPuzzles(): List<Puzzle> {
    return httpJsonClient.get("$endpoint${Puzzle.path}").body()
}

suspend fun getPuzzle(puzzleId: Int): Puzzle {
    return httpJsonClient.get("$endpoint${Puzzle.path}/$puzzleId").body()
}
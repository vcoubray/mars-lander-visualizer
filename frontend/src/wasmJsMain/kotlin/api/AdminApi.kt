package api

import io.ktor.client.*
import io.ktor.client.request.*

class AdminApi(
    private val client: HttpClient,
    private val base: String
) {
    suspend fun reset() { client.post("$base/admin/reset")}
}
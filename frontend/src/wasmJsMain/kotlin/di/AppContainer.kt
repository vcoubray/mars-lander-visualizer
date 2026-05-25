package di

import api.AdminApi
import api.BenchmarksApi
import api.PuzzlesApi
import api.SimulationsApi
import api.buildHttpClient
import baseUrl

data class AppContainer(
    val simulationsApi: SimulationsApi,
    val benchmarksApi: BenchmarksApi,
    val puzzlesApi: PuzzlesApi,
    val adminApi: AdminApi,
)


fun buildAppContainer(): AppContainer {
    val client = buildHttpClient()
    val base = baseUrl
    return AppContainer(
        simulationsApi = SimulationsApi(client, base),
        benchmarksApi = BenchmarksApi(client, base),
        puzzlesApi = PuzzlesApi(client, base),
        adminApi = AdminApi(client, base),
    )

}

package ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.setValue
import api.SimulationsApi
import fr.vco.genetic.algorithm.visualizer.SimulationSummary
import ui.UiState

class SimulationsScreenState(private val api: SimulationsApi) {
    var uiState by mutableStateOf<UiState<List<SimulationSummary>>>(UiState.Loading)

    val expandedIds = mutableStateSetOf<Int>()

    suspend fun load() {
        uiState = UiState.Loading
        uiState = try {
            UiState.Success(api.getAll())
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Failed to load simulations")
        }
    }

    suspend fun delete(id: Int) {
        try {
            api.delete(id)
        } finally {
            load()
        }
    }

    fun toggleExpand(id: Int) {
        if (id in expandedIds) expandedIds.remove(id) else expandedIds.add(id)
    }

}
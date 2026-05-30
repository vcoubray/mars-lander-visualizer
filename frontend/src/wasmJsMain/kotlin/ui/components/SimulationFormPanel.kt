package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import di.LocalAppContainer
import fr.vco.genetic.algorithm.visualizer.MarsSettings
import fr.vco.genetic.algorithm.visualizer.Puzzle
import kotlinx.coroutines.launch

@Composable
fun SimulationFormPanel(
    onSubmit: suspend (MarsSettings) -> Unit,
) {
    val puzzlesApi = LocalAppContainer.current.puzzlesApi
    val formState = remember { MarsSimulationFormState() }
    val scope = rememberCoroutineScope()
    var puzzles by remember { mutableStateOf<List<Puzzle>>(emptyList()) }
    var submitting by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { puzzles = puzzlesApi.getAll() }

    Column(
        modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("New Simulation", style = MaterialTheme.typography.headlineSmall)
        GlobalSettingsForm(formState)
        MarsEngineSettingsForm(formState, puzzles)


        Button(
            onClick = {
                scope.launch {
                    submitting = true
                    try {
                        onSubmit(formState.toSettings())
                    } finally {
                        submitting = false
                    }
                }
            },
            enabled = formState.isValid && !submitting,
            modifier = Modifier.fillMaxWidth()
        ) {
            if(submitting) {CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)}
            else Text("Launch simulation")
        }
    }

}
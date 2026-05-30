package ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import di.LocalAppContainer
import fr.vco.genetic.algorithm.visualizer.SimulationStatus
import fr.vco.genetic.algorithm.visualizer.SimulationSummary
import kotlinx.coroutines.launch
import ui.UiState
import ui.components.JsonColors
import ui.components.JsonViewer
import ui.components.SimulationFormPanel

@Composable
fun SimulationsScreen(
    onVisualize: (Int) -> Unit,
) {
    val api = LocalAppContainer.current.simulationsApi
    val state = remember { SimulationsScreenState(api) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        state.load()
    }
    Row (modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f).padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Simulation", style = MaterialTheme.typography.headlineMedium)
                IconButton(onClick = { scope.launch { state.load() } }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                }
            }
            Spacer(Modifier.height(8.dp))

            when (val s = state.uiState) {
                is UiState.Loading -> Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

                is UiState.Error -> Text("Error: ${s.message}", color = MaterialTheme.colorScheme.error)
                is UiState.Success -> {
                    if (s.data.isEmpty()) {
                        Text("No simulation yet. Create one with the form")
                    } else {
                        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(s.data, key = { it.id }) { simulation ->
                                SimulationListItem(
                                    simulation = simulation,
                                    isExpanded = simulation.id in state.expandedIds,
                                    onExpand = { state.toggleExpand(simulation.id) },
                                    onVisualize = { onVisualize(simulation.id) },
                                    onDelete = { scope.launch { state.delete(simulation.id) } },
                                )

                            }
                        }
                    }
                }
            }
        }

        VerticalDivider()

        Box(modifier = Modifier.width(360.dp).fillMaxHeight()) {
            SimulationFormPanel(
                onSubmit = { settings ->
                    api.create(settings)
                    state.load()
                }
            )
        }
    }
}

@Composable
fun SimulationListItem(
    simulation: SimulationSummary,
    isExpanded: Boolean,
    onExpand: () -> Unit,
    onVisualize: () -> Unit,
    onDelete: () -> Unit,
) {
    val chevronRotation by animateFloatAsState(
        targetValue = if (isExpanded) 100f else 0f,
        label = "chevron",
    )

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                when {
                    simulation.status == SimulationStatus.PENDING ->
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)

                    simulation.bestScore >= simulation.simulationSettings.engineSettings.maxScore() ->
                        Icon(Icons.Default.CheckCircle, contentDescription = "Success", tint = Color(0xFF4CAF50))

                    else ->
                        Icon(
                            Icons.Default.Cancel,
                            contentDescription = "Failed",
                            tint = MaterialTheme.colorScheme.error
                        )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "Simulation #${simulation.id}", style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onVisualize) {
                    Icon(Icons.Default.Visibility, contentDescription = "Visualize")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
                IconButton(onClick = onExpand) {
                    Icon(
                        Icons.Default.KeyboardArrowDown, contentDescription = "Toggle expand",
                        modifier = Modifier.rotate(chevronRotation)
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    HorizontalDivider()
                    Spacer(Modifier.height(8.dp))

                    val isDark = !MaterialTheme.colorScheme.background.luminance().let{it > 0.5f}
                    JsonViewer(
                        value = simulation.simulationSettings,
                        colors = if(isDark) JsonColors.dark() else JsonColors.light(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
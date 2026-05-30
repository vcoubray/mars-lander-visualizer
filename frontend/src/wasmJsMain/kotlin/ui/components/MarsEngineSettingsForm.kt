package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.vco.genetic.algorithm.visualizer.Puzzle
import ui.components.input.WeightField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarsEngineSettingsForm(state: MarsSimulationFormState, puzzles: List<Puzzle>) {

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Mars Settings", style = MaterialTheme.typography.titleMedium)

        var expanded by remember { mutableStateOf(false) }
        val selectedPuzzle = puzzles.firstOrNull { it.id == state.puzzleId }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = selectedPuzzle?.title ?: "Select Puzzle",
                onValueChange = {},
                readOnly = true,
                label = { Text("Puzzle") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                puzzles.forEach { puzzle ->
                    DropdownMenuItem(
                        text = { Text(puzzle.title) },
                        onClick = {
                            state.puzzleId = puzzle.id
                            expanded = false
                        })

                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            WeightField("Speed max", state.speedMax, {state.speedMax = it}, Modifier.weight(1f))
            WeightField("X speed w.", state.xSpeedWeight, {state.xSpeedWeight = it}, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            WeightField("y speed w.", state.ySpeedWeight, {state.ySpeedWeight = it}, Modifier.weight(1f))
            WeightField("Rotate w.", state.rotateWeight, {state.rotateWeight = it}, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            WeightField("Distance w.", state.distanceWeight, {state.distanceWeight = it}, Modifier.weight(1f))
            WeightField("Crash speed w.", state.crashSpeedWeight, {state.crashSpeedWeight = it}, Modifier.weight(1f))
        }

    }

}
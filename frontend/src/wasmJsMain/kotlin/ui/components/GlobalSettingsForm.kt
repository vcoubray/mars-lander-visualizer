package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.vco.genetic.algorithm.visualizer.CrossoverType
import fr.vco.genetic.algorithm.visualizer.LimitType
import fr.vco.genetic.algorithm.visualizer.SelectionType
import ui.components.input.EnumDropDown
import ui.components.input.LabeledSlider


@Composable
fun GlobalSettingsForm(state: MarsSimulationFormState) {

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Algorithm settings", style = MaterialTheme.typography.titleSmall)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            EnumDropDown(
                label = "Limit type",
                value = state.limitType,
                options = LimitType.entries,
                onSelected = { state.limitType = it },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = state.limitValue,
                onValueChange = { state.limitValue = it },
                label = { Text("Value") },
                isError = state.limitValueError != null,
                supportingText = state.limitValueError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = state.chromosomeSize,
                onValueChange = { state.chromosomeSize = it },
                label = { Text("Chromosome size") },
                isError = state.chromosomeSizeError != null,
                supportingText = state.chromosomeSizeError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = state.populationSize,
                onValueChange = { state.populationSize = it },
                label = { Text("Population size") },
                isError = state.populationSizeError != null,
                supportingText = state.populationSizeError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        LabeledSlider("Mutation probability", state.mutationProbability, 0.0..0.5) {
            state.mutationProbability = it
        }

        LabeledSlider("Elitism %", state.elitismPercent, 0.0..1.0) {
            state.elitismPercent = it
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            EnumDropDown(
                "Selection",
                state.selectionType,
                SelectionType.entries,
                { state.selectionType = it },
                Modifier.weight(1f)
            )

            EnumDropDown(
                "Crossover",
                state.crossoverType,
                CrossoverType.entries,
                { state.crossoverType = it },
                Modifier.weight(1f)
            )
        }

    }
}



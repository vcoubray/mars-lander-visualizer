package ui.components.input

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import utils.format

@Composable
fun LabeledSlider(
    label: String,
    value: Double,
    range: ClosedFloatingPointRange<Double>,
    onValueChange: (Double) -> Unit
) {
    Column {
        Text("$label : ${value.format(2)}", style = MaterialTheme.typography.bodySmall)
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toDouble()) },
            valueRange = range.start.toFloat()..range.endInclusive.toFloat(),
        )

    }
}
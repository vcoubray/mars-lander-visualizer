import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val container = document.getElementById("composeApplication")
        ?: error("Could not find #composeApplication div")
    ComposeViewport(container) {
        var darkMode by remember { mutableStateOf(false) }
        AppTheme(darkMode = darkMode) {
            AppShell(
                darkMode = darkMode,
                onToggleDarkMode = { darkMode = !darkMode },
                onResetDatabase       = { /* TODO step 16 */ },
            )
        }
    }
}

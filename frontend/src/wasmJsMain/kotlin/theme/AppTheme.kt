import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFFB71C1C),
    onPrimary = Color.White,
    secondary = Color(0xFF795548),
    background = Color(0xFFFAF9F8),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1C1B1F)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFEF9A9A),
    onPrimary = Color(0xFF5F0000),
    secondary = Color(0xFFBCAAA4),
    background = Color(0xFF1A1A1A),
    surface = Color(0xFF2C2C2C),
    onSurface = Color(0xFFE6E1E5),
)

@Composable
fun AppTheme(darkMode: Boolean = false, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkMode) DarkColors else LightColors,
        content = content
    )
}
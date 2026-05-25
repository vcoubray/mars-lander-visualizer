package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.json.Json

data class JsonColors(
    val key: Color,
    val string: Color,
    val number: Color,
    val boolean: Color,
    val nullValue: Color,
    val punctuation: Color,
    val background: Color,
) {
    companion object {
        fun dark() = JsonColors(
            key = Color(0xFF80CBC4),  // teal
            string = Color(0xFFFFCC80),  // amber
            number = Color(0xFFA5D6A7),  // green
            boolean = Color(0xFFCE93D8),  // purple
            nullValue = Color(0xFF90A4AE),  // blue-grey
            punctuation = Color(0xFFB0BEC5),
            background = Color(0xFF1E1E1E),
        )

        fun light() = JsonColors(
            key = Color(0xFF00695C),
            string = Color(0xFFE65100),
            number = Color(0xFF2E7D32),
            boolean = Color(0xFF6A1B9A),
            nullValue = Color(0xFF546E7A),
            punctuation = Color(0xFF607D8B),
            background = Color(0xFFF5F5F5),
        )
    }
}

fun colorizeJson(json: String, colors: JsonColors): AnnotatedString = buildAnnotatedString {
    var i = 0
    var expectingKey = true   // true after '{' and after ','

    fun peek() = if (i < json.length) json[i] else ' '
    fun advance() = json[i++]

    fun appendStyled(text: String, color: Color) {
        withStyle(SpanStyle(color = color)) { append(text) }
    }

    while (i < json.length) {
        when (val ch = advance()) {
            '{', '[' -> {
                appendStyled(ch.toString(), colors.punctuation)
                expectingKey = ch == '{'
            }

            '}', ']' -> appendStyled(ch.toString(), colors.punctuation)
            ':' -> {
                appendStyled(":", colors.punctuation)
                expectingKey = false
            }

            ',' -> {
                appendStyled(",", colors.punctuation)
                // After a comma inside an object we expect a key again.
                // This is heuristic: track brace depth for correctness in nested objects.
                // For our use-case (well-formed server JSON) this works fine.
                expectingKey = true
            }

            '"' -> {
                // Read until closing unescaped quote
                val sb = StringBuilder("\"")
                while (i < json.length) {
                    val c = advance()
                    sb.append(c)
                    if (c == '\\' && i < json.length) {
                        sb.append(advance())
                    } else if (c == '"') break
                }
                val tokenStr = sb.toString()
                val isKey = expectingKey && peek().let { p ->
                    p == ':' || json.indexOf(':', i).let { idx ->
                        idx != -1 && json.substring(i, idx).isBlank()
                    }
                }
                appendStyled(tokenStr, if (isKey) colors.key else colors.string)
            }

            in '0'..'9', '-' -> {
                val sb = StringBuilder(ch.toString())
                while (i < json.length && (peek().isDigit() || peek() == '.' || peek() == 'e' || peek() == 'E'
                            || peek() == '+' || peek() == '-')
                ) sb.append(advance())
                appendStyled(sb.toString(), colors.number)
            }

            't' -> {
                // true
                val literal = if (json.startsWith("true", i - 1)) "true" else ch.toString()
                if (literal == "true") {
                    i += 3; appendStyled("true", colors.boolean)
                } else append(ch.toString())
            }

            'f' -> {
                // true
                val literal = if (json.startsWith("false", i - 1)) "false" else ch.toString()
                if (literal == "false") {
                    i += 4; appendStyled("false", colors.boolean)
                } else append(ch.toString())
            }

            'n' -> {
                // true
                val literal = if (json.startsWith("null", i - 1)) "null" else ch.toString()
                if (literal == "null") {
                    i += 3; appendStyled("null", colors.boolean)
                } else append(ch.toString())
            }

            else -> append(ch.toString())   // whitespace, newlines pass through unstyled
        }
    }
}

@Composable
fun JsonViewer(
    json: String,
    colors: JsonColors = JsonColors.dark(),
    modifier: Modifier = Modifier
) {
    val annotated = remember(json, colors) { colorizeJson(json, colors) }

    Box(
        modifier = modifier
            .background(colors.background, shape = MaterialTheme.shapes.small)
            .padding(12.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        Text(
            text = annotated,
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp
        )
    }
}


val prettyJson = Json { prettyPrint = true; ignoreUnknownKeys = true }

@Composable
inline fun <reified T> JsonViewer(
    value: T,
    colors: JsonColors = JsonColors.dark(),
    modifier: Modifier = Modifier,
) {
    JsonViewer(json = prettyJson.encodeToString(value), colors = colors, modifier = modifier)
}
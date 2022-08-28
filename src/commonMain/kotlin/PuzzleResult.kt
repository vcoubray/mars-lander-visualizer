import kotlinx.serialization.Serializable


@Serializable
data class PuzzleResult(
    var generationCount: Int,
    var executionTime: Long
)
import kotlinx.serialization.Serializable


@Serializable
data class RunStats(
    var generationCount: Int,
    var executionTime: Long
)
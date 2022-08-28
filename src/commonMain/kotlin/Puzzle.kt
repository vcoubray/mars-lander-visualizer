import codingame.State
import kotlinx.serialization.Serializable

@Serializable
data class Puzzle(
    val id: Int,
    val title: String,
    val surface: String,
    val initialState: State
) {

    companion object {
        val path = "/puzzles"
    }
}

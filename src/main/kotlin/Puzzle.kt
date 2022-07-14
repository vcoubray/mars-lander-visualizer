
data class Puzzle (
    val id: Int,
    val title: String,
    val surface: String
)

val puzzles = listOf(
    Puzzle(1,"Facile à droite","0.0 100.0 1000.0 500.0 1500.0 1500.0 3000.0 1000.0 4000.0 150.0 5500.0 150.0 6999.0 800.0"),
    Puzzle(2,"Vitesse d'entrée, bon côté","0.0 100.0 1000.0 500.0 1500.0 100.0 3000.0 100.0 3500.0 500.0 3700.0 200.0 5000.0 1500.0 5800.0 300.0 6000.0 1000.0 6999.0 2000.0")
)


val puzzleMap = puzzles.associateBy { it.id }
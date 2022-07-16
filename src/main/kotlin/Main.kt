import csstype.NamedColor
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement
import react.create
import react.dom.client.createRoot

fun main() {

    var currentPuzzle = puzzles.first()

    val canvas = document.createElement("canvas") as HTMLCanvasElement
    val context = createCanvas(canvas, 700, 300)
    context.init(currentPuzzle)

    val algoOptions = AlgoOptions(
        chromosomeSize = 60,
        populationSize = 10,
        puzzle = currentPuzzle,
        onGeneration = {
            for (chromosome in it ) {
                val color = when {
                    chromosome.score < 50.0 -> NamedColor.orange
                    chromosome.score < 100 -> NamedColor.yellow
                    else -> NamedColor.green
                }
                context.drawPath(chromosome.path.map { (x, y) -> x / 10 to y / 10 }, color)
            }
        },
        onInit = { puzzle ->
            context.init(puzzle)
        }
    )
    val algo = GeneticAlgorithm(algoOptions)

    val container = document.createElement("div")
    document.body!!.appendChild(container)
    val app = App.create {
        this.canvas = context
        this.onPuzzleChange = { puzzle ->
            currentPuzzle = puzzle
            algo.updateOptions(puzzle)
        }
        this.onNext = {
            context.init(currentPuzzle)
            algo.next()
        }
        this.onReset = {
            algo.updateOptions(currentPuzzle)
        }
    }
    createRoot(container).render(app)
}





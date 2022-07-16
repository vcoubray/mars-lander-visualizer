
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement
import react.create
import react.dom.client.createRoot

fun main() {

    val canvas = document.createElement("canvas") as HTMLCanvasElement
    val context = createCanvas(canvas, 700, 300)

    val currentPuzzle = puzzles.first()
    val algoOptions = AlgoOptions(
        chromosomeSize = 60,
        populationSize = 60,
        puzzle = currentPuzzle,
        onChange = {surface, population, generation ->
            context.drawAlgo(surface,population, generation)

        }
    )
    val algo = GeneticAlgorithm(algoOptions)

    val container = document.createElement("div")
    document.body!!.appendChild(container)
    val app = App.create {
        this.puzzle = currentPuzzle
        this.onPuzzleChange = { puzzle ->
            algo.updateOptions(puzzle)
        }
        this.onNext = {
            algo.next()
        }
        this.onReset = {
            algo.reset()
        }
    }
    createRoot(container).render(app)
}





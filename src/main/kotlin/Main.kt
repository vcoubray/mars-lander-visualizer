import Components.App
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement
import react.create
import react.dom.client.createRoot


fun main() {

    val canvas = document.createElement("canvas") as HTMLCanvasElement
    val context = createCanvas(canvas, 7000, 3000)

    val currentPuzzle = PUZZLES.first()
    val algoSettings = AlgoSettings(
        chromosomeSize = 60,
        populationSize = 60,
        mutationProbability = 0.2,
        puzzle = currentPuzzle,
    )

    val algo = GeneticAlgorithm(
        algoSettings,
        onChange = { surface, population, generation ->
            context.drawAlgo(surface, population, generation)
        }
    )

    val container = document.createElement("div")
    document.body!!.appendChild(container)
    val app = App.create {
        this.puzzles = PUZZLES
        this.algoSettings = algoSettings
        this.onUpdateSettings = {
            algo.updateSettings(it)
        }
        this.onNext = {
            algo.next()
        }
    }
    createRoot(container).render(app)
}





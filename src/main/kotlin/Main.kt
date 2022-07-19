import kotlinx.browser.document
import kotlinx.js.timers.Timeout
import kotlinx.js.timers.clearInterval
import kotlinx.js.timers.setInterval
import org.w3c.dom.HTMLCanvasElement
import react.create
import react.dom.client.createRoot

fun main() {

    val canvas = document.createElement("canvas") as HTMLCanvasElement
    val context = createCanvas(canvas, 7000, 3000)

    val currentPuzzle = puzzles.first()
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
    var intervalId : Timeout? = null
    val app = App.create {
        this.algoSettings = algoSettings
        this.onUpdateSettings = {
            intervalId?.let(::clearInterval)
            algo.updateSettings(it)
        }
        this.onNext = {
            algo.next()
        }
        this.onPlay = {
            intervalId = setInterval(algo::next,100 )
        }
        this.onStop = {
            intervalId?.let(::clearInterval)
        }
    }
    createRoot(container).render(app)
}





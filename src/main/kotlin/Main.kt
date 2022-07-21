import Components.App
import kotlinx.browser.document
import react.create
import react.dom.client.createRoot

fun main() {

//    val canvas = document.createElement("canvas") as HTMLCanvasElement
//    val context = createCanvas(canvas, 7000, 3000)

    val defaultSettings = AlgoSettings(
        chromosomeSize = 60,
        populationSize = 10,
        mutationProbability = 0.2,
        puzzle =  PUZZLES.first(),
    )

    val container = document.createElement("div")
    document.body!!.appendChild(container)
    val app = App.create {
        this.puzzles = PUZZLES
        this.algoSettings = defaultSettings
    }
    createRoot(container).render(app)
}





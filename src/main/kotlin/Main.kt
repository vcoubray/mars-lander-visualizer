import ui.App
import kotlinx.browser.document
import react.create
import react.dom.client.createRoot

fun main() {

//    val defaultSettings = AlgoSettings(
//        chromosomeSize = 180,
//        populationSize = 80,
//        mutationProbability = 0.02,
//        elitismPercent = 0.2,
//        puzzle = PUZZLES.first(),
//        100.0,
//        30.0,
//        50.0,
//        10.0,
//        110.0,
//        0.9
//    )

    val container = document.createElement("div")
    document.body!!.appendChild(container)
    val app = App.create()

//    val app = Visualizer.create {
//        this.puzzles = PUZZLES
//        this.algoSettings = defaultSettings
//    }
    createRoot(container).render(app)
}





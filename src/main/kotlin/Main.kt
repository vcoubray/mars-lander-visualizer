import components.App
import kotlinx.browser.document
import react.create
import react.dom.client.createRoot

fun main() {

    val defaultSettings = AlgoSettings(
        chromosomeSize = 180,
        populationSize = 80,
        mutationProbability = 0.01,
        elitismPercent = 0.1,
        puzzle =  PUZZLES.first(),
        100.0,
        35.0,
        35.0,
        10.0,
        120.0
    )

    val container = document.createElement("div")
    document.body!!.appendChild(container)
    val app = App.create {
        this.puzzles = PUZZLES
        this.algoSettings = defaultSettings
    }
    createRoot(container).render(app)
}





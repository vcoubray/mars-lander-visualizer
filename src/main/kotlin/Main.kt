import components.App
import kotlinx.browser.document
import react.create
import react.dom.client.createRoot

fun main() {

    val defaultSettings = AlgoSettings(
        chromosomeSize = 180,
        populationSize = 60,
        mutationProbability = 0.01,
        elitismPercent = 0.2,
        puzzle =  PUZZLES.first(),
        250.0,
        0.4
    )

    val container = document.createElement("div")
    document.body!!.appendChild(container)
    val app = App.create {
        this.puzzles = PUZZLES
        this.algoSettings = defaultSettings
    }
    createRoot(container).render(app)
}





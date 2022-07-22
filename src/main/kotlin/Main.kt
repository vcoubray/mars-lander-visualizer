import Components.App
import kotlinx.browser.document
import react.create
import react.dom.client.createRoot

fun main() {

    val defaultSettings = AlgoSettings(
        chromosomeSize = 60,
        populationSize = 10,
        mutationProbability = 0.01,
        elitismPercent = 0.1,
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





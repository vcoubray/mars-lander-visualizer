import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement
import react.create
import react.dom.client.createRoot
import react.ref
import react.useState


fun main() {

    var currentPuzzle = puzzles.first()

    val canvas = document.createElement("canvas") as HTMLCanvasElement
    val context = createCanvas(canvas, 700, 300)
    context.init(currentPuzzle)


    val container = document.createElement("div")
    document.body!!.appendChild(container)
    val app = App.create {
        this.canvas = context
        this.onPuzzleChange = { puzzle ->
            currentPuzzle = puzzle
            context.init(currentPuzzle)
        }

        this.onReset = {
            context.init(currentPuzzle)
        }
    }
    createRoot(container).render(app)
}





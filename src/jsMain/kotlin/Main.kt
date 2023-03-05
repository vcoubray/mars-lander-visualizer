import components.AppPicoCss
import kotlinx.browser.document
import react.create
import react.dom.client.createRoot

fun main() {
    val container = document.createElement("div")
        .also(document.body!!::appendChild)
//    createRoot(container).render(App.create())
    createRoot(container).render(AppPicoCss.create())
}

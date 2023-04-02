
import components.App
import kotlinx.browser.document
import react.create
import react.dom.client.createRoot
import theme.ThemeService


fun main() {
    kotlinext.js.require("./scss/main.scss")
    ThemeService.init()

    val container = document.createElement("div")
        .also(document.body!!::appendChild)
    createRoot(container).render(App.create())

}

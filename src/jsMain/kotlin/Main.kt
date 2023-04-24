
import components.App
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import react.create
import react.dom.client.createRoot
import theme.ThemeService

val mainScope = MainScope()

fun main() {
    kotlinext.js.require("./scss/main.scss")
    ThemeService.init()

    val container = document.createElement("div")
        .also(document.body!!::appendChild)
    createRoot(container).render(App.create())

}


import components.App
import kotlinx.coroutines.MainScope
import react.create
import react.dom.client.createRoot
import libs.theme.ThemeService
import libs.wrappers.require
import web.dom.document

val mainScope = MainScope()

fun main() {
    require("./scss/main.scss")
    ThemeService.init()

    val container = document.createElement("div")
        .also(document.body::appendChild)
    createRoot(container).render(App.create())

}

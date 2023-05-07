import hooks.usePages
import js.core.jso
import react.create
import react.router.RouteObject
import react.router.dom.createBrowserRouter
import react.useMemo

fun useRouter() = useMemo {
    createBrowserRouter(
        routes = usePages().mapIndexed { i, page ->
            jso<RouteObject> {
                index = i == 0
                path = page.url
                element = page.component.create()
            }
        }.toTypedArray()
    )
}
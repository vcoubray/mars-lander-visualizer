package components

import react.FC
import react.Props
import react.router.RouterProvider
import useRouter

val App = FC<Props> {

    RouterProvider {
        this.router = useRouter()
    }
}




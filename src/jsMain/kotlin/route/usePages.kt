package route

import route.pages.Components
import route.pages.GenerationPage
import route.pages.SimulationsPage
import libs.models.Page
import mui.icons.material.Extension
import mui.icons.material.Home
import mui.icons.material.Rocket
import mui.icons.material.VisibilitySharp
import react.useMemo

fun usePages(): Set<Page>{
    return useMemo {
        setOf(
            Page("/", "Home", Home, Components, visible = true),
            Page("/simulations", "Simulations", Rocket , SimulationsPage),
            Page("/simulations/:simulationId", "Simulations", VisibilitySharp, GenerationPage, visible = false),
            Page("/components", "Components", Extension , Components)
        )
    }
}

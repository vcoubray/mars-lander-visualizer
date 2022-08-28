package mui.utils

import kotlinx.js.jso
import mui.material.styles.Theme
import mui.material.styles.TransitionCreateOptions

object MuiTransitions {
    val openSidebarTransition = { theme: Theme ->
        jso<TransitionCreateOptions> {
            easing = theme.transitions.easing.sharp
            duration = theme.transitions.duration.leavingScreen
        }
    }
    val closeSidebarTransition = { theme: Theme ->
        jso<TransitionCreateOptions> {
            easing = theme.transitions.easing.easeOut
            duration = theme.transitions.duration.enteringScreen
        }
    }
}
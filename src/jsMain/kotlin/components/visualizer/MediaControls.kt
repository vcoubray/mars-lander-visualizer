package components.visualizer

import csstype.px
import kotlinx.js.timers.Timeout
import modules.ThemeContext
import mui.icons.material.NavigateNextSharp
import mui.icons.material.PlayArrowSharp
import mui.icons.material.RefreshSharp
import mui.icons.material.StopSharp
import mui.material.*
import mui.system.responsive
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.create
import react.dom.onChange
import react.useContext

external interface MediaControlsProps : Props {
    var isPlaying: Boolean
    var autoStop: Boolean
    var onPlay: () -> Unit
    var onStop: () -> Unit
    var onNext: () -> Unit
    var onReset: () -> Unit
    var toggleAutoStop: (Boolean) -> Unit
}

val MediaControls = FC<MediaControlsProps> { props ->

    val theme by useContext(ThemeContext)

    Box {
        sx {
            marginTop = theme.spacing(2)
        }
        FormGroup {
            FormControlLabel {
                sx {
                    marginLeft = 0.px
                }
                label = Typography.create { +"Stop when landing" }
                labelPlacement = LabelPlacement.start
                control = Switch.create {
                    defaultChecked = props.autoStop
                    onChange = { _, value ->
                        props.toggleAutoStop(value)
                    }
                }
            }
        }

        Stack {
            direction = responsive(StackDirection.row)
            spacing = responsive(2)
            Button {
                +"Next"
                startIcon = NavigateNextSharp.create()
                variant = ButtonVariant.contained
                disabled = props.isPlaying

                onClick = {
                    props.onNext()
                }
            }

            if (!props.isPlaying) {
                Button {
                    +"Play"
                    startIcon = PlayArrowSharp.create()
                    variant = ButtonVariant.contained


                    onClick = {
                        props.onPlay()
                    }
                }
            } else {
                Button {
                    +"Stop"
                    startIcon = StopSharp.create()
                    variant = ButtonVariant.contained

                    onClick = {
                        props.onStop()
                    }
                }
            }
            Button {
                +"Reset"
                startIcon = RefreshSharp.create()
                variant = ButtonVariant.outlined
                onClick = {
                    props.onReset()
                }
            }
        }
    }

}
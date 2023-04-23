package models

import mui.icons.material.SvgIconComponent
import react.FC
import react.Props

data class Page(
    val url: String,
    val label: String,
    val icon: SvgIconComponent,
    val component: FC<Props>,
    val visible: Boolean = true,
)



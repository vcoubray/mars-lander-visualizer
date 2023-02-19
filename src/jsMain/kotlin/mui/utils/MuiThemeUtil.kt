package mui.utils

import mui.material.styles.Theme

/**
 *  Cast emotion theme to MUI Theme
 */
inline fun emotion.react.Theme.toMuiTheme() = this.unsafeCast<Theme>()




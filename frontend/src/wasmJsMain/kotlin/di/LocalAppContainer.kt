package di

import androidx.compose.runtime.compositionLocalOf

val LocalAppContainer = compositionLocalOf<AppContainer>{
    error("No AppContainer provided - wrap your tree in CompositionLocalProvider(LocalAppContainer provides …)")
}
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.browser.window
import navigation.Routes
import ui.SideMenu
import ui.screens.BenchmarkDetailScreen
import ui.screens.BenchmarksScreen
import ui.screens.SimulationsScreen
import ui.screens.VisualizationScreen

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
fun AppShell(
    darkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onResetDatabase: () -> Unit,
) {
    val navController = rememberNavController()
    var menuExpanded  by remember { mutableStateOf(true) }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { entry ->
            val route = entry.destination.route ?: return@collect
            window.history.pushState(null, "", "/#$route")
        }
    }

    Row(modifier = Modifier.fillMaxSize()) {
        // Left panel
        SideMenu(
            expanded = menuExpanded,
            onToggle = { menuExpanded  = !menuExpanded },
            onNavigateSimulations = {
                navController.navigate(Routes.Simulations) { launchSingleTop = true }
            },
            onNavigateBenchmarks = {
                navController.navigate(Routes.Benchhmarks) { launchSingleTop = true }
            },
            onResetDatabase = onResetDatabase,
        )


        // Content Area
        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
            NavHost(
                navController = navController,
                startDestination = Routes.Simulations,
                modifier = Modifier.fillMaxSize(),
            ) {
                composable<Routes.Simulations> { SimulationsScreen() }
                composable<Routes.Benchhmarks> { BenchmarksScreen() }
                composable<Routes.Visualization> { entry ->
                    VisualizationScreen(simulationId = entry.toRoute<Routes.Visualization>().simulationId)
                }
                composable<Routes.BenchmarkDetail> {entry ->
                    BenchmarkDetailScreen(benchmarkId =  entry.toRoute<Routes.BenchmarkDetail>().benchmarkId)
                }
            }

            IconButton(
                onClick = onToggleDarkMode,
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
            ) {
                Icon(
                    imageVector = if (darkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = if (darkMode) "Switch to light mode" else "Switch to dark mode",
                )
            }
        }
    }
}
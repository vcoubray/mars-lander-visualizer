package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SideMenu(
    expanded: Boolean,
    onToggle: () -> Unit,
    onNavigateSimulations: () -> Unit,
    onNavigateBenchmarks: () -> Unit,
    onResetDatabase: () -> Unit,
    modifier: Modifier = Modifier
) {

    var settingsOpen by remember { mutableStateOf(false) }

    val width by animateDpAsState(
        targetValue = if (expanded) 240.dp else 64.dp,
        animationSpec = tween(durationMillis = 200),
        label = "sideMenuWidth"
    )

    Column(
        modifier = modifier
            .width(width)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surfaceContainer),
    ) {

        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = if (expanded) Icons.Default.Close else Icons.Default.Menu,
                    contentDescription = "Toggle menu",
                )
            }
            AnimatedVisibility(visible = expanded) {
                Text(
                    "Mars Lander",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = modifier.padding(start = 8.dp),
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Clip,
                )
            }
        }

        HorizontalDivider()
        // Navigation

        MenuNavItem(
            icon = Icons.Default.RocketLaunch,
            label = "Simulations",
            expanded = expanded,
            onClick = onNavigateSimulations,
        )
        MenuNavItem(
            icon = Icons.Default.BarChart,
            label = "Benchmarks",
            expanded = expanded,
            onClick = onNavigateBenchmarks,
        )

        Spacer(Modifier.weight(1f))
        HorizontalDivider()

        // Settings Item
        MenuNavItem(
            icon = Icons.Default.Settings,
            label = "Settings",
            expanded = expanded,
            trailingIcon = if (expanded) {
                if (settingsOpen) Icons.Default.ExpandLess else Icons.Default.ExpandMore
            } else null,
            onClick = {if(expanded) settingsOpen = !settingsOpen }
        )

        AnimatedVisibility(visible = expanded && settingsOpen) {
            Column (modifier = Modifier.padding(start = 16.dp)){
                TextButton(
                    onClick = onResetDatabase,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Icon(Icons.Default.DeleteForever, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Reset database",
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Clip,)
                }
            }
        }
    }


}

@Composable
private fun MenuNavItem(
    icon: ImageVector,
    label: String,
    expanded: Boolean,
    trailingIcon: ImageVector? = null,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Icon(imageVector = icon, contentDescription = label)
        AnimatedVisibility(visible = expanded) {
            Row(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    modifier = Modifier.weight(1f).padding(start = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Clip,
                )
                if(trailingIcon != null) {
                    Icon(imageVector = trailingIcon, contentDescription = null)
                }
            }
        }

    }
}
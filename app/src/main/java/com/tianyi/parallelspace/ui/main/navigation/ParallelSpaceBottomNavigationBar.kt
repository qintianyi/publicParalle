package com.tianyi.parallelspace.ui.main.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute

@Composable
fun ParallelSpaceBottomNavigationBar(
    currentDestination: NavDestination?,
    navigateToTopLevelDestination: (ParallelSpaceTopLevelDestination) -> Unit
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        TOP_LEVEL_DESTINATIONS.forEach { destination ->
            NavigationBarItem(
                selected = currentDestination.hasRoute(destination),
                onClick = { navigateToTopLevelDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = stringResource(id = destination.iconTextId)
                    )
                }
            )
        }
    }
}

fun NavDestination?.hasRoute(destination: ParallelSpaceTopLevelDestination): Boolean =
    this?.hasRoute(destination.route::class) ?: false
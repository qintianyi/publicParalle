/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tianyi.parallelspace.ui.main.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AddToHomeScreen
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.tianyi.parallelspace.R
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable data object Home : Route
    @Serializable data object Add : Route
    @Serializable data object PP : Route
}

sealed class ParallelSpaceTopLevelDestination(
    val route: Route,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
){
    data object HomeDestination : ParallelSpaceTopLevelDestination(
    route = Route.Home,
    selectedIcon = Icons.Default.Home,
    unselectedIcon = Icons.Default.Home,
    iconTextId = R.string.tab_home
    )

    data object AddDestination: ParallelSpaceTopLevelDestination(
    route = Route.Add,
    selectedIcon = Icons.AutoMirrored.Filled.AddToHomeScreen,
    unselectedIcon = Icons.AutoMirrored.Filled.AddToHomeScreen,
    iconTextId = R.string.tab_add
    )
}

class ParallelSpaceNavigationActions(private val navController: NavHostController) {

    fun navigateTo(destination: ParallelSpaceTopLevelDestination) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}

val TOP_LEVEL_DESTINATIONS = listOf(
    ParallelSpaceTopLevelDestination.HomeDestination,
    ParallelSpaceTopLevelDestination.AddDestination
)

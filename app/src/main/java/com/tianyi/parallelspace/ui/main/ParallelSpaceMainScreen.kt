package com.tianyi.parallelspace.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tianyi.parallelspace.ui.ParallelSpaceViewModel
import com.tianyi.parallelspace.ui.main.add.ParallelSpaceAddScreen
import com.tianyi.parallelspace.ui.main.home.ParallelSpaceHomeScreen
import com.tianyi.parallelspace.ui.main.navigation.ParallelSpaceBottomNavigationBar
import com.tianyi.parallelspace.ui.main.navigation.ParallelSpaceNavigationActions
import com.tianyi.parallelspace.ui.main.navigation.ParallelSpaceTopLevelDestination
import com.tianyi.parallelspace.ui.main.navigation.Route

@Composable
fun ParallelSpaceMainScreen(
    viewModel: ParallelSpaceViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        ParallelSpaceNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            ParallelSpaceBottomNavigationBar(
                currentDestination,
                navigationActions::navigateTo) },
        topBar = { ParallelSpaceTopAppBar(currentDestination, navigationActions::navigateTo)})
    { paddingValues ->
        ParallelSpaceNavHost(
            viewModel = viewModel,
            navigateToTopLevelDestination = navigationActions::navigateTo,
            navController = navController,
            startDestination = Route.Home,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),

            )
    }
}

@Composable
fun ParallelSpaceNavHost(
    viewModel: ParallelSpaceViewModel,
    navigateToTopLevelDestination: (ParallelSpaceTopLevelDestination) -> Unit,
    navController: NavHostController,
    startDestination: Route,
    modifier: Modifier = Modifier) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ){
        composable<Route.Home>{ ParallelSpaceHomeScreen(mainViewModel = viewModel,onNavToAddScreen = {navigateToTopLevelDestination(
            ParallelSpaceTopLevelDestination.AddDestination)}) }
        composable<Route.Add>{ ParallelSpaceAddScreen(mainViewModel =  viewModel) }
    }
}
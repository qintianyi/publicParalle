package com.tianyi.parallelspace.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tianyi.parallelspace.ui.main.ParallelSpaceMainScreen
import com.tianyi.parallelspace.ui.main.add.ParallelSpaceAddScreen
import com.tianyi.parallelspace.ui.main.home.ParallelSpaceHomeScreen
import com.tianyi.parallelspace.ui.main.navigation.ParallelSpaceBottomNavigationBar
import com.tianyi.parallelspace.ui.main.navigation.ParallelSpaceNavigationActions
import com.tianyi.parallelspace.ui.main.navigation.ParallelSpaceTopLevelDestination
import com.tianyi.parallelspace.ui.main.navigation.Route
import com.tianyi.parallelspace.ui.pp.ParallelSpacePrivacyPolicyScreen

@ExperimentalMaterial3Api
@Composable
fun ParallelSpaceApp() {
    var showPP by remember { mutableStateOf(true) }
    if (showPP) {
        ParallelSpacePrivacyPolicyScreen(){
            showPP = false
        }
    } else {
        ParallelSpaceMainScreen()
    }
}





package com.tianyi.parallelspace.ui.main.home

import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.virtual.VirtualApi
import com.tianyi.parallelspace.data.AppRuntime
import com.tianyi.parallelspace.data.model.AppInfo
import com.tianyi.parallelspace.data.model.UiState
import com.tianyi.parallelspace.data.model.VirtualSpaceInfo
import com.tianyi.parallelspace.ui.ParallelSpaceViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ParallelSpaceHomeViewModel(private val mainViewModel: ParallelSpaceViewModel): ViewModel(){
    val uiState: StateFlow<UiState<List<VirtualSpaceInfo>>> = mainViewModel.userSpaceFlow.asStateFlow()

    fun onItemClick(it: AppInfo, spaceInfo: VirtualSpaceInfo) {
        VirtualApi.launchPackage(it.packageName, spaceInfo.id, 0)
    }

    fun onAppDeleteClick(appInfo: AppInfo, spaceInfo: VirtualSpaceInfo) {
        mainViewModel.uninstallAppFromVirtualSpace(spaceInfo, appInfo)
    }

    fun onPageDeleteClick(spaceInfo: VirtualSpaceInfo) {
        mainViewModel.uninstallVirtualSpace(spaceInfo)
    }
}

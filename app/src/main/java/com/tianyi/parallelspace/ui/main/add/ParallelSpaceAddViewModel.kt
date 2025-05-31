package com.tianyi.parallelspace.ui.main.add

import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tianyi.parallelspace.data.AppRuntime
import com.tianyi.parallelspace.data.model.AppInfo
import com.tianyi.parallelspace.data.model.UiEvent
import com.tianyi.parallelspace.data.model.UiState
import com.tianyi.parallelspace.data.model.VirtualSpaceInfo
import com.tianyi.parallelspace.ui.ParallelSpaceViewModel
import com.tianyi.parallelspace.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ParallelSpaceAddViewModel(private val mainViewModel: ParallelSpaceViewModel): ViewModel() {
    val uiState: StateFlow<UiState<List<AppInfo>>> = mainViewModel.deviceInstalledPackageChannel.asStateFlow()

    fun getInstallAvailableSpace(appInfo: AppInfo): Flow<UiState<List<VirtualSpaceInfo>>> {
        return mainViewModel.userSpaceFlow.map {
            if (it is UiState.Success) {
                // add new space
                it.copy(
                    it.data.filter { spaceInfo -> !spaceInfo.virtualAppList.contains(appInfo) } + mainViewModel.createNewSpace()
                )
            } else if (it is UiState.Empty) {
                UiState.Success(listOf(mainViewModel.createNewSpace()))
            }
            else {
                it
            }
        }
    }

    fun confirmInstall(installApp: AppInfo, selectSpaces: List<VirtualSpaceInfo>) {
        mainViewModel.installAppToVirtualSpace(selectSpaces, installApp)
    }
}

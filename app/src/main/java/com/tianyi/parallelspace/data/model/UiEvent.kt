package com.tianyi.parallelspace.data.model

sealed class UiEvent {
    data class ShowConfirmInstallDialog(val appInfo: AppInfo, val availableSpaceInfoList: List<VirtualSpaceInfo>) : UiEvent()
    data class ShowConfirmUninstallDialog(val spaceInfo: VirtualSpaceInfo, val appInfo: AppInfo) : UiEvent()

}
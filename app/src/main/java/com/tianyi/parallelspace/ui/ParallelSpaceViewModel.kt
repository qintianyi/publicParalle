package com.tianyi.parallelspace.ui

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.virtual.VirtualApi
import com.tianyi.parallelspace.data.AppRuntime
import com.tianyi.parallelspace.data.model.AppInfo
import com.tianyi.parallelspace.data.model.UiState
import com.tianyi.parallelspace.data.model.VirtualSpaceInfo
import com.tianyi.parallelspace.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.SortedMap

class ParallelSpaceViewModel: ViewModel() {
    private val _userSpaceMap: SortedMap<Int, VirtualSpaceInfo> = sortedMapOf()
    private val _deviceInstalledPkgList: MutableList<AppInfo> = mutableListOf()

    val userSpaceFlow = MutableStateFlow<UiState<List<VirtualSpaceInfo>>>(UiState.Loading)//MutableStateFlow<List<VirtualSpaceInfo>>(emptyList())
    val deviceInstalledPackageChannel = MutableStateFlow<UiState<List<AppInfo>>>(UiState.Loading)//MutableStateFlow<List<AppInfo>>(emptyList())

    init {
        viewModelScope.launch {
            val packageManager = AppRuntime.application.packageManager
            val array = VirtualApi.getAllUserSpace() // tab num

            if (array.isEmpty()) {
                userSpaceFlow.update { UiState.Empty }
            } else {
                array.sortedArray().forEach{ spaceId->
                    val installedPkgs = VirtualApi.getInstalledPackageNames(PackageManager.MATCH_UNINSTALLED_PACKAGES, spaceId)
                    val virtualAppList = installedPkgs.mapNotNull { pkg->
                        VirtualApi.getPackageInfo(pkg, 0, spaceId).applicationInfo?.let {
                            AppInfo(it.packageName, it.loadLabel(packageManager).toString(), it.loadIcon(packageManager))
                        }
                    }
                    _userSpaceMap[spaceId] = VirtualSpaceInfo(spaceId, virtualAppList = virtualAppList)
                }
                userSpaceFlow.update { UiState.Success(_userSpaceMap.values.toList()) }
            }
        }

        viewModelScope.launch {
            val packageManager = AppRuntime.application.packageManager
            val installedPackages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
            installedPackages.filter {
                (it.applicationInfo?.flags?.and(ApplicationInfo.FLAG_SYSTEM)) == 0
            }.forEach { packageInfo->
                packageInfo.applicationInfo?.let {
                    if (packageInfo.packageName == Utils.PACKAGE_NAME_CHROME || Utils.getAppAbi(packageInfo) == AppRuntime.abi) {
                        val appInfo = AppInfo(packageInfo.packageName, it.loadLabel(packageManager).toString(), it.loadIcon(packageManager))
                        _deviceInstalledPkgList.add(appInfo)
                    }
                }
            }
            if (_deviceInstalledPkgList.isEmpty()) {
                deviceInstalledPackageChannel.update { UiState.Empty }
            } else {
                deviceInstalledPackageChannel.update { UiState.Success(_deviceInstalledPkgList) }
            }
        }
    }

    fun uninstallAppFromVirtualSpace(spaceInfo: VirtualSpaceInfo, appInfo: AppInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            userSpaceFlow.update { UiState.Loading }

            val isSuccess = VirtualApi.uninstallPackage(appInfo.packageName, spaceInfo.id) == 1
            if (isSuccess) {
                _userSpaceMap[spaceInfo.id] = spaceInfo.copy(virtualAppList = spaceInfo.virtualAppList.toMutableList() - appInfo)
                if (_userSpaceMap[spaceInfo.id]?.virtualAppList?.isEmpty() == true) {
                    _userSpaceMap.remove(spaceInfo.id)
                }
                userSpaceFlow.update {
                    UiState.Success(_userSpaceMap.values.toList())
                }
            } else {
                // TODO: notify
            }
        }
    }

    fun uninstallVirtualSpace(spaceInfo: VirtualSpaceInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            userSpaceFlow.update { UiState.Loading }
            spaceInfo.virtualAppList.forEach {
                VirtualApi.uninstallPackage(it.packageName, spaceInfo.id)
            }
            _userSpaceMap.remove(spaceInfo.id)
            userSpaceFlow.update {
                UiState.Success(_userSpaceMap.values.toList())
            }
        }
    }

    fun installAppToVirtualSpace(spaceInfoList: List<VirtualSpaceInfo>, appInfo: AppInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            userSpaceFlow.update { UiState.Loading }
            spaceInfoList.forEach{ spaceInfo->
                val spaceId = spaceInfo.id
                val isSuccess = VirtualApi.clonePackage(appInfo.packageName, spaceId,  true) == 1
                if (isSuccess) {
                    if (_userSpaceMap[spaceId] == null) {
                        // new space
                        _userSpaceMap[spaceId] =
                            VirtualSpaceInfo(spaceId, virtualAppList = listOf(appInfo))
                    } else {
                        _userSpaceMap[spaceId] =
                            spaceInfo.copy(virtualAppList = spaceInfo.virtualAppList + appInfo)
                    }
                    userSpaceFlow.update {
                        UiState.Success(_userSpaceMap.values.toList())
                    }
                } else {
                    userSpaceFlow.update {
                        UiState.Error("Install failed")
                    }
                }
            }
        }
    }

    fun createNewSpace(): VirtualSpaceInfo {
        return VirtualSpaceInfo(findSmallestMissing(_userSpaceMap.keys), emptyList(), spaceName = "New Space")
    }

    fun findSmallestMissing(nums: Set<Int>): Int {
        val numSet = nums.toSortedSet()
        var smallestMissing = 0

        while (smallestMissing in numSet) {
            smallestMissing++
        }

        return smallestMissing
    }

//    fun installAppToVirtualSpace(spaceInfo: VirtualSpaceInfo?, appInfo: AppInfo) {
//        viewModelScope.launch(Dispatchers.IO) {
//            userSpaceFlow.update { UiState.Loading }
//
//            val spaceId = spaceInfo?.id ?:  ((_userSpaceMap.keys.lastOrNull() ?: -1) + 1)
//            val isSuccess = VirtualApi.clonePackage(appInfo.packageName, spaceId,  true) == 1
//
//            if (isSuccess) {
//                _userSpaceMap[spaceId] = spaceInfo?.copy(virtualAppList = spaceInfo.virtualAppList + appInfo)
//                    ?: VirtualSpaceInfo(spaceId, listOf(appInfo))
//
//                userSpaceFlow.update {
//                    UiState.Success(_userSpaceMap.values.toList())
//                }
//            } else {
//                // TODO: notify
//            }
//        }
//
//    }

}
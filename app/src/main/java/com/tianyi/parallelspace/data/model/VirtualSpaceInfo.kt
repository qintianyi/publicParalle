package com.tianyi.parallelspace.data.model

import androidx.compose.runtime.snapshots.SnapshotStateList

data class VirtualSpaceInfo(
    val id: Int,
    val virtualAppList: SnapshotStateList<AppInfo>,
    val spaceName: String = "Space ${id+1}",
)
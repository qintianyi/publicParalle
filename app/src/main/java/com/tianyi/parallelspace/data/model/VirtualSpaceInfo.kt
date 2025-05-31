package com.tianyi.parallelspace.data.model

data class VirtualSpaceInfo(
    val id: Int,
    val virtualAppList: List<AppInfo>,
    val spaceName: String = "Space ${id+1}",
){
    override fun equals(other: Any?): Boolean {
        return id == (other as? VirtualSpaceInfo)?.id
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + spaceName.hashCode()
        return result
    }
}
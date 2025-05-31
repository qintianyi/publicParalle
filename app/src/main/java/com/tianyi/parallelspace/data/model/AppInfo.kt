package com.tianyi.parallelspace.data.model

import android.graphics.drawable.Drawable

data class AppInfo(
    val packageName: String,
    val appName: String,
    val appIcon: Drawable,
    val frozen: Boolean = false
){
    override fun equals(other: Any?): Boolean {
        return packageName == (other as? AppInfo)?.packageName
    }

    override fun hashCode(): Int {
        var result = packageName.hashCode()
        result = 31 * result + appName.hashCode()
        return result
    }
}

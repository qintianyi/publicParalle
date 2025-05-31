package com.tianyi.parallelspace.util

import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.bumptech.virtual.VirtualApi
import com.tianyi.parallelspace.data.model.AppInfo
import com.tianyi.parallelspace.data.model.VirtualSpaceInfo
import java.io.File

object Utils {
    const val PACKAGE_NAME_CHROME = "com.android.chrome"

    fun getAppAbi(packageInfo: PackageInfo): String {
        try {
            val abi = packageInfo.applicationInfo?.nativeLibraryDir?.let { File(it).name }
            return abi.orEmpty()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return "Package not found"
        }
    }

    fun installAppToVirtualSpace(spaceInfo: VirtualSpaceInfo?, appInfo: AppInfo) {
        VirtualApi.installPackageFromHost(spaceInfo?.id ?: 0, appInfo.packageName, true)
    }

    fun initVirtualSdk(context: Context){
        VirtualApi.install(context, null)
    }
}
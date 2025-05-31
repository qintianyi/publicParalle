package com.tianyi.parallelspace.data

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.tianyi.parallelspace.util.Utils
import java.io.File

object AppRuntime {
    lateinit var application: Application
    lateinit var abi: String

    fun init(application: Application) {
        this.application = application
        val packageInfo: PackageInfo = application.packageManager.getPackageInfo(application.packageName, PackageManager.GET_META_DATA)
        this.abi = Utils.getAppAbi(packageInfo)
    }


}
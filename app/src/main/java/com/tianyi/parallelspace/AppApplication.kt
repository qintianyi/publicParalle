package com.tianyi.parallelspace

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.tianyi.parallelspace.data.AppRuntime
import java.io.File

class AppApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppRuntime.init(this)
    }
}
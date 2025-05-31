package com.tianyi.parallelspace

import android.app.Application
import com.tianyi.parallelspace.data.AppRuntime
import com.tianyi.parallelspace.util.Utils

class AppApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppRuntime.init(this)
        Utils.initVirtualSdk(this)
    }
}
package com.tianyi.parallelspace

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tianyi.parallelspace.theme.ParallelSpaceAppTheme
import com.tianyi.parallelspace.ui.ParallelSpaceApp
import com.tianyi.parallelspace.ui.main.add.ParallelSpaceAddViewModel
import com.tianyi.parallelspace.ui.main.home.ParallelSpaceHomeViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ParallelSpaceAppTheme {
                ParallelSpaceApp()
            }
        }
    }
}
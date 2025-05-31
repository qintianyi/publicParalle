package com.tianyi.parallelspace.ui.pp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tianyi.parallelspace.data.model.UiState
import com.tianyi.parallelspace.ui.widget.ParallelSpacePrivacyPolicyTopAppBar

@Composable
fun ParallelSpacePrivacyPolicyScreen(viewModel: ParallelSpacePrivacyPolicyViewModel = viewModel()) {
    Scaffold(
        topBar = { ParallelSpacePrivacyPolicyTopAppBar() }
    ) { paddingValues ->
        ParallelSpacePrivacyPolicyContent(paddingValues = paddingValues, viewModel.pp)
    }
}


@Composable
fun ParallelSpacePrivacyPolicyContent(paddingValues: PaddingValues, ppContent: String) {
    Surface {
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(Modifier.fillMaxSize()) {
                Text(text = ppContent)
            }

            Column {
                Row {
                    Checkbox(checked = false, onCheckedChange = {})
                    Text(text = "I agree to the Privacy Policy")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Continue")
                }
            }
        }

    }

}

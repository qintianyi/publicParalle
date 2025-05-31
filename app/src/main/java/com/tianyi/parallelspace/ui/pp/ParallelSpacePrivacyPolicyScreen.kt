package com.tianyi.parallelspace.ui.pp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tianyi.parallelspace.data.model.UiState
import com.tianyi.parallelspace.ui.widget.ParallelSpacePrivacyPolicyTopAppBar

@Composable
fun ParallelSpacePrivacyPolicyScreen(viewModel: ParallelSpacePrivacyPolicyViewModel = viewModel(), onContinue: ()-> Unit) {
    Scaffold(
        topBar = { ParallelSpacePrivacyPolicyTopAppBar() }
    ) { paddingValues ->
        val state: UiState<String> by viewModel.pp.collectAsStateWithLifecycle()

        when (state) {
            is UiState.Loading ->  CircularProgressIndicator(modifier = Modifier.size(64.dp))
            is UiState.Success -> ParallelSpacePrivacyPolicyContent(paddingValues = paddingValues, (state as UiState.Success<String>).data, onContinue)
            else -> {}
        }

    }
}


@Composable
fun ParallelSpacePrivacyPolicyContent(paddingValues: PaddingValues, ppContent: String, onContinue: ()-> Unit) {
    var agreePP by remember { mutableStateOf(false) }
    Surface {
        Column(modifier = Modifier.padding(paddingValues)) {
            Column(Modifier.weight(1f, fill = true)) {
                Text(text = ppContent)
            }

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = agreePP, onCheckedChange = { agreePP = !agreePP})
                    Text(text = "I agree to the Privacy Policy")
                }
                Button(onClick = onContinue, enabled = agreePP) {
                    Text(text = "Continue")
                }
            }
        }

    }

}

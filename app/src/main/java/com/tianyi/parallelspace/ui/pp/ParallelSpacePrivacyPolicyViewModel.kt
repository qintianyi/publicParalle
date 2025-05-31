package com.tianyi.parallelspace.ui.pp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tianyi.parallelspace.data.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ParallelSpacePrivacyPolicyViewModel: ViewModel() {

    private val _pp = MutableStateFlow<UiState<String>>(UiState.Loading)
    val pp = _pp.asStateFlow()

    init {
        viewModelScope.launch {
            // TODO:
            _pp.value = UiState.Success("Privacy Policy")
        }
    }
}
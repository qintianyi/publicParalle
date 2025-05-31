package com.tianyi.parallelspace.data.model

sealed class UiState<out T: Any> {
    data object Loading : UiState<Nothing>()
    data object Empty : UiState<Nothing>()
    data class Success<out T: Any>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}


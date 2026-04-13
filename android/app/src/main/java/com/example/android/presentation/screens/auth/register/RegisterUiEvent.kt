package com.example.android.presentation.screens.auth.register

sealed class RegisterUiEvent {
    object Success : RegisterUiEvent()
    data class ShowSnackbar(val message: String) : RegisterUiEvent()
}
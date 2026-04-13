package com.example.android.presentation.screens.auth.login

sealed class LoginUiEvent {
    object Success : LoginUiEvent()
    data class Error(val message: String) : LoginUiEvent()
}
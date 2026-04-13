package com.example.android.presentation.screens.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.domain.repository.UserRepository
import com.example.android.data.remote.dto.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    private val _uiEvent = MutableSharedFlow<LoginUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onLoginClick() {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                _uiEvent.emit(LoginUiEvent.Error("Vui lòng nhập đầy đủ thông tin"))
                return@launch
            }

            isLoading = true
            val result = userRepository.login(LoginRequest(email, password))

            result.onSuccess {
                _uiEvent.emit(LoginUiEvent.Success)
            }.onFailure {
                _uiEvent.emit(LoginUiEvent.Error(it.message ?: "Đăng nhập thất bại"))
            }
            isLoading = false
        }
    }
}
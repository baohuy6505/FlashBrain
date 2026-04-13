package com.example.android.presentation.screens.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.data.remote.dto.RegisterRequest
import com.example.android.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // State quản lý dữ liệu nhập liệu
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    private val _uiEvent = MutableSharedFlow<RegisterUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onRegisterClick() {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            viewModelScope.launch {
                _uiEvent.emit(RegisterUiEvent.ShowSnackbar("Vui lòng điền đầy đủ thông tin"))
            }
            return
        }

        viewModelScope.launch {
            isLoading = true
            val request = RegisterRequest(name, email, password)
            val result = userRepository.register(request)

            result.onSuccess {
                _uiEvent.emit(RegisterUiEvent.Success)
            }.onFailure {
                _uiEvent.emit(RegisterUiEvent.ShowSnackbar(it.message ?: "Đăng ký thất bại"))
            }
            isLoading = false
        }
    }
}
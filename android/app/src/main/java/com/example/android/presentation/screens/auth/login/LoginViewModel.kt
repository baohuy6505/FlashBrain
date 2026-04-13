package com.example.android.presentation.screens.auth.login

import android.content.Context
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
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    private val _uiEvent = MutableSharedFlow<LoginUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    // ── ĐĂNG NHẬP EMAIL/PASSWORD ──
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

    // ── ĐĂNG NHẬP GOOGLE (Hàm mới) ──
    fun onGoogleLoginClick(context: Context) {
        viewModelScope.launch {
            val credentialManager = CredentialManager.create(context)

            // 1. Dùng Client ID của bạn làm "chìa khóa" để xin Token
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("729101777691-4ftppia2htrnau8i3m4r7u8ts0q9rli2.apps.googleusercontent.com")
                .setAutoSelectEnabled(true)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            try {
                isLoading = true

                // 2. Lệnh này sẽ bật hộp thoại chọn tài khoản Google trên điện thoại
                val result = credentialManager.getCredential(context = context, request = request)
                val credential = result.credential

                // 3. Trích xuất idToken THẬT từ kết quả người dùng chọn
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val realIdToken = googleIdTokenCredential.idToken

                // 4. Gửi cái realIdToken (chuỗi eyJhb...) này lên Server
                val apiResult = userRepository.loginWithGoogle(realIdToken)

                apiResult.onSuccess {
                    _uiEvent.emit(LoginUiEvent.Success)
                }.onFailure {
                    _uiEvent.emit(LoginUiEvent.Error(it.message ?: "Server từ chối Token"))
                }
            } catch (e: Exception) {
                // Trường hợp người dùng nhấn quay lại, không chọn tài khoản
                _uiEvent.emit(LoginUiEvent.Error("Bạn đã hủy đăng nhập Google"))
            } finally {
                isLoading = false
            }
        }
    }
}
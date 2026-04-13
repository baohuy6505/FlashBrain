package com.example.android.presentation.screens.profile

import android.util.Log // 1. QUAN TRỌNG: Thêm import này để dùng Log.d/Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.data.local.SessionManager
import com.example.android.domain.model.User
import com.example.android.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // Dữ liệu User lấy từ SessionManager
    private val _userData = MutableStateFlow<User?>(sessionManager.fetchUser())
    val userData: StateFlow<User?> = _userData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Sử dụng Channel để truyền sự kiện UI (Snackbar, Navigation)
    private val _uiEvent = Channel<ProfileUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    // 1. Logic Đăng xuất
    fun logout() {
        viewModelScope.launch {
            Log.d("PROFILE_DEBUG", "2. ViewModel bắt đầu gọi Repository.logout()")
            _isLoading.value = true

            val result = userRepository.logout()

            result.onSuccess {
                Log.d("PROFILE_DEBUG", "3. Repository báo Logout THÀNH CÔNG")
                // Xóa dữ liệu user cục bộ để UI cập nhật (nếu cần)
                _userData.value = null
                _uiEvent.send(ProfileUiEvent.LogoutSuccess)
            }.onFailure {
                Log.e("PROFILE_DEBUG", "3. Repository báo lỗi: ${it.message}")
                _uiEvent.send(ProfileUiEvent.ShowSnackbar(it.message ?: "Đăng xuất thất bại"))
            }
            _isLoading.value = false
        }
    }

    // 2. Cập nhật Profile
    fun updateProfile(name: String, imagePath: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = userRepository.updateProfile(name, imagePath)

            result.onSuccess { updatedUser ->
                sessionManager.saveUser(updatedUser)
                _userData.value = updatedUser
                _uiEvent.send(ProfileUiEvent.ShowSnackbar("Cập nhật thành công!"))
            }.onFailure {
                _uiEvent.send(ProfileUiEvent.ShowSnackbar(it.message ?: "Cập nhật thất bại"))
            }
            _isLoading.value = false
        }
    }
}

// 3. Định nghĩa các sự kiện UI
sealed class ProfileUiEvent {
    data class ShowSnackbar(val message: String) : ProfileUiEvent()
    object LogoutSuccess : ProfileUiEvent()
}
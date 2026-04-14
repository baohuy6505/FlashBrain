package com.example.android.presentation.screens.profile

import android.util.Log
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

    // Lấy dữ liệu từ SessionManager làm giá trị khởi tạo ban đầu
    private val _userData = MutableStateFlow<User?>(sessionManager.fetchUser())
    val userData: StateFlow<User?> = _userData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Sử dụng Channel để truyền sự kiện UI (Snackbar, Navigation)
    private val _uiEvent = Channel<ProfileUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    // Khởi tạo: Tự động gọi API lấy thông tin mới nhất khi mở màn hình
    init {
        fetchUserProfile()
    }

    // 1. Lấy thông tin cá nhân từ Server
    fun fetchUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = userRepository.getMe()

            result.onSuccess { updatedUser ->
                // Cập nhật cả Session cục bộ và StateFlow để UI hiển thị ngay
                sessionManager.saveUser(updatedUser)
                _userData.value = updatedUser
            }.onFailure {
                _uiEvent.send(ProfileUiEvent.ShowSnackbar(it.message ?: "Không thể làm mới dữ liệu"))
            }
            _isLoading.value = false
        }
    }

    // 2. Logic Đăng xuất
    fun logout() {
        viewModelScope.launch {
            Log.d("PROFILE_DEBUG", "ViewModel bắt đầu gọi logout")
            _isLoading.value = true

            val result = userRepository.logout()

            result.onSuccess {
                Log.d("PROFILE_DEBUG", "Đăng xuất thành công")
                _userData.value = null
                _uiEvent.send(ProfileUiEvent.LogoutSuccess)
            }.onFailure {
                Log.e("PROFILE_DEBUG", "Lỗi đăng xuất: ${it.message}")
                _uiEvent.send(ProfileUiEvent.ShowSnackbar(it.message ?: "Đăng xuất thất bại"))
            }
            _isLoading.value = false
        }
    }

    // 3. Cập nhật Profile (Tên và Ảnh)
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

// Định nghĩa các sự kiện UI
sealed class ProfileUiEvent {
    data class ShowSnackbar(val message: String) : ProfileUiEvent()
    object LogoutSuccess : ProfileUiEvent()
}
package com.example.android.presentation.screens.profile

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

    // Dữ liệu User lấy từ SessionManager ngay khi khởi tạo
    private val _userData = MutableStateFlow<User?>(sessionManager.fetchUser())
    val userData: StateFlow<User?> = _userData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Channel đảm bảo thông báo chỉ xuất hiện 1 lần duy nhất
    private val _uiEvent = Channel<ProfileUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

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

sealed class ProfileUiEvent {
    data class ShowSnackbar(val message: String) : ProfileUiEvent()
}
package com.example.android.presentation.screens.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.domain.model.Notification
import com.example.android.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationUiState(
    val notifications: List<Notification> = emptyList(),
    val unreadCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    init {
        // Lắng nghe local DB real-time
        observeNotifications()
        observeUnreadCount()
        // Tự động sync khi mở màn hình
        syncFromServer()
    }

    private fun observeNotifications() {
        repository.getNotifications()
            .onEach { list ->
                _uiState.update { it.copy(notifications = list) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeUnreadCount() {
        repository.getUnreadCount()
            .onEach { count ->
                _uiState.update { it.copy(unreadCount = count) }
            }
            .launchIn(viewModelScope)
    }

    fun syncFromServer() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.syncFromServer()
                .onFailure { e ->
                    // Lỗi mạng → vẫn hiển thị data cũ, chỉ show toast nhẹ
                    _uiState.update { it.copy(errorMessage = "Không thể tải thông báo mới") }
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun markAsRead(id: String) {
        viewModelScope.launch { repository.markAsRead(id) }
    }

    fun markAllAsRead() {
        viewModelScope.launch { repository.markAllAsRead() }
    }
}
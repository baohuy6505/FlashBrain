package com.example.android.domain.repository

import com.example.android.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    // Stream danh sách notification từ local DB (real-time)
    fun getNotifications(): Flow<List<Notification>>

    // Stream số lượng chưa đọc
    fun getUnreadCount(): Flow<Int>

    // Sync từ server về local
    suspend fun syncFromServer(): Result<Unit>

    // Đánh dấu đã đọc
    suspend fun markAsRead(id: String)
    suspend fun markAllAsRead()

    // Lập lịch nhắc nhở hằng ngày (WorkManager)
    suspend fun scheduleDailyReminder()
}
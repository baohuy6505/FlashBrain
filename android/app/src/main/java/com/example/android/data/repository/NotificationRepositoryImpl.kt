package com.example.android.data.repository

import android.content.Context
import androidx.work.*
import com.example.android.data.local.dao.NotificationDao
import com.example.android.data.mapper.toDomain
import com.example.android.data.mapper.toEntity
import com.example.android.data.remote.NotificationApi
import com.example.android.domain.model.Notification
import com.example.android.domain.repository.NotificationRepository
import com.example.android.data.worker.NotificationWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDao: NotificationDao,
    private val notificationApi: NotificationApi,
    @ApplicationContext private val context: Context
) : NotificationRepository {

    // 1. Stream real-time từ Room
    override fun getNotifications(): Flow<List<Notification>> {
        return notificationDao.getAllNotifications()
            .map { entities -> entities.map { it.toDomain() } }
    }

    // 2. Stream số badge chưa đọc
    override fun getUnreadCount(): Flow<Int> {
        return notificationDao.getUnreadCount()
    }

    // 3. Sync từ server → lưu local (Offline-First)
    override suspend fun syncFromServer(): Result<Unit> {
        return try {
            val response = notificationApi.getNotifications()
            if (response.isSuccessful) {
                val dtos = response.body()?.data ?: emptyList()
                val entities = dtos.map { it.toEntity() }
                notificationDao.upsertAll(entities)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Server error: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Lỗi mạng → vẫn hiển thị data local, không crash app
            Result.failure(e)
        }
    }

    // 4. Đánh dấu đã đọc
    override suspend fun markAsRead(id: String) {
        notificationDao.markAsRead(id)
    }

    override suspend fun markAllAsRead() {
        notificationDao.markAllAsRead()
    }

    // 5. WorkManager lập lịch nhắc hằng ngày
    override suspend fun scheduleDailyReminder() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag("daily_reminder")
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_reminder_work",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
package com.example.android.data.local.dao

import androidx.room.*
import com.example.android.data.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    // --- DÀNH CHO WORKER (Dữ liệu học tập) ---

    // Đếm số thẻ đến hạn học (Huy nhớ check tên bảng và tên cột bên FlashcardEntity nhé)
    @Query("SELECT COUNT(*) FROM flashcards WHERE nextReviewDate <= :today")
    suspend fun countDueCards(today: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Update
    suspend fun updateNotification(notification: NotificationEntity)


    // --- DÀNH CHO UI (Màn hình Thông báo) ---

    @Query("SELECT * FROM notifications ORDER BY createdAt DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Upsert
    suspend fun upsertAll(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Query("DELETE FROM notifications WHERE createdAt < :threshold")
    suspend fun deleteOlderThan(threshold: String)
}
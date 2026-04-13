package com.example.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val scheduledAt: String,
    val isSent: Boolean = false,
    val isRead: Boolean = false,        // ← Thêm trạng thái đọc
    val createdAt: String = ""
)
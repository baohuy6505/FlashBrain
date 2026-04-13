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
    val isActive: Boolean = true,
    val isSent: Boolean = false,
    val isRead: Boolean = false,
    val createdAt: String = ""
)
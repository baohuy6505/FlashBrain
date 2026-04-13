package com.example.android.domain.model

data class Notification(
    val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val scheduledAt: String,
    val isActive: Boolean = true,
    val isSent: Boolean = false,
    val isRead: Boolean = false,        // ← Thêm field này
    val createdAt: String = ""
)
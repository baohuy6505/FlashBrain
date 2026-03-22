package com.example.android.domain.model

data class Notification(
    val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val scheduledAt: Long,
    val isSent: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

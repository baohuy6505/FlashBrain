package com.example.android.data.remote.dto

data class NotificationDto(
    val _id: String,
    val user_id: String,
    val title: String,
    val message: String,
    val scheduled_at: String,
    val created_at: String
)
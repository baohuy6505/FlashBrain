package com.example.android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NotificationDto(
    @SerializedName("_id")
    val id: String,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("daily_time") // 👈 Khớp với "daily_time" trong Node.js Schema
    val dailyTime: String,

    @SerializedName("is_active") // 👈 Thêm trường này từ Schema Node.js
    val isActive: Boolean,

    @SerializedName("created_at")
    val createdAt: String
)
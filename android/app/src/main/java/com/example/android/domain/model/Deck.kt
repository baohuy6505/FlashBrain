package com.example.android.domain.model

data class Deck(
    val id: String,
    val userId: String,
    val title: String,
    val isPublic: Boolean = false,
    val isDeleted: Boolean = false,
    val createdAt: String,
    val updatedAt: String,
    val isDirty: Boolean = false, // Đánh dấu cần đồng bộ
    val serverId: String? = null,
)
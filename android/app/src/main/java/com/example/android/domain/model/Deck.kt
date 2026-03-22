package com.example.android.domain.model

data class Deck(
    val id: String,
    val userId: String,
    val title: String,
    val isPublic: Boolean = false,
    val isDeleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

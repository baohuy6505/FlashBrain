package com.example.android.domain.model

data class User(
    val id: String,
    val email: String,
    val role: String = "USER",
    val subscriptionType: String = "FREE",
    val balance: Double = 0.0,
    val proExpireAt: Long? = null,
    val isDeleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

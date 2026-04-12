package com.example.android.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val image: String? = null,
    val role: String = "USER",
    val subscriptionType: String = "FREE",
    val balance: Double = 0.0,
    val proExpireAt: Long? = null,
    val isDeleted: Boolean = false,
    val createdAt: String = "", // Gán giá trị mặc định là chuỗi rỗng
    val updatedAt: String? = null
)

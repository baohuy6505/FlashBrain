package com.example.android.data.remote.dto

import com.example.android.domain.model.User

data class UserRemoteDTO(
    val id: String,
    val name: String,
    val email: String,
    val image: String? = null,
    val role: String? = "USER",
    val subscriptionType: String? = "FREE",
    val balance: Double? = 0.0,
    val proExpireAt: Long? = null,
    val isDeleted: Boolean? = false,
    val createdAt: String? = "",
    val updatedAt: String? = null
) {
    fun toDomain(): User {
        return User(
            id = id,
            name = name,
            email = email,
            image = image,
            role = role ?: "USER",
            subscriptionType = subscriptionType ?: "FREE",
            balance = balance ?: 0.0,
            proExpireAt = proExpireAt,
            isDeleted = isDeleted ?: false,
            createdAt = createdAt ?: "",
            updatedAt = updatedAt
        )
    }
}
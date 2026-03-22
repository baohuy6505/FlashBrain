package com.example.android.domain.model

data class ProPackage(
    val id: String,
    val name: String,
    val price: Double,
    val durationDays: Int,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

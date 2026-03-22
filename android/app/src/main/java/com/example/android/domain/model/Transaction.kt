package com.example.android.domain.model

data class Transaction(
    val id: String,
    val userId: String,
    val type: String, // "DEPOSIT" | "BUY_PACKAGE"
    val amount: Double,
    val packageId: String? = null,
    val status: String = "PENDING", // "SUCCESS" | "FAILED"
    val createdAt: Long = System.currentTimeMillis()
)

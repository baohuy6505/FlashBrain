package com.example.android.presentation.screens.premium

data class SubscriptionPackage(
    val title: String,
    val subtitle: String,
    val price: String,
    val period: String,
    val features: List<String>,
    val isPopular: Boolean = false,
    val buttonText: String
)

data class Transaction(
    val title: String,
    val date: String,
    val amount: String,
    val status: TransactionStatus
)

enum class TransactionStatus(val text: String) {
    SUCCESS("SUCCESS"),
    PENDING("PENDING"),
    FAILED("FAILED")
}
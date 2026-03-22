package com.example.android.domain.model

data class Flashcard(
    val id: String,
    val deckId: String,
    val frontText: String,
    val backText: String,

    // Các chỉ số phục vụ thuật toán SM-2
    val interval: Int = 0,
    val repetition: Int = 0,
    val easeFactor: Double = 2.5,
    val nextReviewDate: Long? = null,

    val lastReviewedAt: Long? = null,
    val isDeleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

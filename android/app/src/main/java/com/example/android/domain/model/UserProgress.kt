package com.example.android.domain.model

data class UserProgress(
    val userId: String,
    val totalLearned: Int = 0,
    val streakDays: Int = 0,
    val lastStudyDate: Long? = null,
    val updatedAt: Long = System.currentTimeMillis()
)

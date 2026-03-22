package com.example.android.domain.model

data class StudyLog(
    val id: String,
    val userId: String,
    val flashcardId: String,
    val grade: Int,
    val createdAt: Long = System.currentTimeMillis()
)

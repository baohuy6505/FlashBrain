package com.example.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val userId: String,
    val totalLearned: Int = 0,
    val streakDays: Int = 0,
    val lastStudyDate: Long? = null,
    val updatedAt: Long = System.currentTimeMillis()
)
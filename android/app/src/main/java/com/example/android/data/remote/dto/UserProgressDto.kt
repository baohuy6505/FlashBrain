package com.example.android.data.remote.dto

data class UserProgressDto(
    val user_id: String,
    val total_learned: Int = 0,
    val streak_days: Int = 0,
    val last_study_date: String? = null,
    val updated_at: String? = null
)
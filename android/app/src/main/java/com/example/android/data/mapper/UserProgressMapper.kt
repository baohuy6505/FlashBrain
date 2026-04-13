package com.example.android.data.mapper

import com.example.android.data.local.entity.UserProgressEntity
import com.example.android.data.remote.dto.UserProgressDto
import com.example.android.domain.model.UserProgress
import java.text.SimpleDateFormat
import java.util.*

fun UserProgressDto.toEntity(): UserProgressEntity {
    val lastStudyMs = try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        sdf.parse(last_study_date ?: "")?.time
    } catch (e: Exception) { null }

    return UserProgressEntity(
        userId = user_id,
        totalLearned = total_learned,
        streakDays = streak_days,
        lastStudyDate = lastStudyMs,
        updatedAt = System.currentTimeMillis()
    )
}

fun UserProgressEntity.toDomain(): UserProgress {
    return UserProgress(
        userId = userId,
        totalLearned = totalLearned,
        streakDays = streakDays,
        lastStudyDate = lastStudyDate,
        updatedAt = updatedAt
    )
}
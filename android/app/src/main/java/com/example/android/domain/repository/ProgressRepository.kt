package com.example.android.domain.repository

import com.example.android.domain.model.Deck
import com.example.android.domain.model.UserProgress
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {
    // Stream real-time từ local DB
    fun getProgress(): Flow<UserProgress?>

    // Sync từ server
    suspend fun syncFromServer(): Result<Unit>
}
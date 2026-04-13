package com.example.android.data.local.dao

import androidx.room.*
import com.example.android.data.local.entity.UserProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {

    @Query("SELECT * FROM user_progress LIMIT 1")
    fun getProgress(): Flow<UserProgressEntity?>

    @Upsert
    suspend fun upsert(entity: UserProgressEntity)
}
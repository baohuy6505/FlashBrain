package com.example.android.data.repository

import com.example.android.data.local.dao.UserProgressDao
import com.example.android.data.mapper.toDomain
import com.example.android.data.mapper.toEntity
import com.example.android.data.remote.ProgressApi
import com.example.android.domain.model.UserProgress
import com.example.android.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProgressRepositoryImpl @Inject constructor(
    private val progressDao: UserProgressDao,
    private val progressApi: ProgressApi
) : ProgressRepository {

    // 1. Luôn đọc từ local DB → UI không bao giờ trống
    override fun getProgress(): Flow<UserProgress?> {
        return progressDao.getProgress()
            .map { entity ->
                entity?.toDomain() // Đảm bảo đã import com.example.android.data.mapper.toDomain
            }
    }

    // 2. Gọi API → lưu local → Flow tự động emit lại
    override suspend fun syncFromServer(): Result<Unit> {
        return try {
            val response = progressApi.getMyProgress()
            if (response.isSuccessful) {
                response.body()?.data?.let { dto ->
                    progressDao.upsert(dto.toEntity())
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Server error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
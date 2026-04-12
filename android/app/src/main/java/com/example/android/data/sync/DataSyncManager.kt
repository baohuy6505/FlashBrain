package com.example.android.data.sync

import android.content.Context
import androidx.work.*
import com.example.android.data.local.dao.DeckDao
import com.example.android.data.local.dao.FlashcardDao
import com.example.android.data.mapper.toDomain
import com.example.android.data.mapper.toDto
import com.example.android.data.remote.FlashcardApi
import com.example.android.data.worker.FlashcardSyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSyncManager @Inject constructor(
    private val flashcardDao: FlashcardDao,
    private val deckDao: DeckDao,
    private val flashcardApi: FlashcardApi,
    @ApplicationContext private val context: Context
) {
    // Huy dán full token của Huy vào đây nhé
    private val testToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJhYzVmMjllOC0yZDdmLTQ4YzEtODA0OC05OTI3YWNiODk1OTYiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc3NjAwNzYxOCwiZXhwIjoxNzc2NjEyNDE4fQ.nNXA-86JUUy3aEZmmJ80Dnn9Ym6Qb_iDHNk_6qaiL2g"

    /**
     * 1. Đồng bộ Bộ bài (Decks)
     * Phải chạy trước Flashcards để có serverId cho quan hệ cha-con
     */
    suspend fun syncDecks() {
        val dirtyDecks = deckDao.getDirtyDecks()
        if (dirtyDecks.isEmpty()) return

        android.util.Log.d("HUY_DEBUG", "SyncManager: Đang đẩy ${dirtyDecks.size} bộ bài lên Atlas...")

        dirtyDecks.forEach { entity ->
            try {
                val deckDto = entity.toDomain().toDto()

                // Phân luồng: Nếu chưa có serverId thì POST, có rồi thì PUT
                val response = if (entity.serverId.isNullOrEmpty()) {
                    flashcardApi.createDeck(testToken, deckDto)
                } else {
                    flashcardApi.updateDeck(testToken, entity.serverId, deckDto)
                }

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val serverData = apiResponse?.data // Bóc lớp ApiResponse<T>

                    val updatedEntity = entity.copy(
                        isDirty = false,
                        serverId = serverData?.id ?: entity.serverId // Lưu lại ID từ MongoDB
                    )
                    deckDao.insertOrUpdate(updatedEntity)
                    android.util.Log.d("HUY_DEBUG", "Sync DECK thành công: ${entity.title}")
                }
            } catch (e: Exception) {
                android.util.Log.e("HUY_DEBUG", "Lỗi sync DECK ${entity.id}: ${e.message}")
            }
        }
    }

    /**
     * 2. Đồng bộ Thẻ (Flashcards)
     */
    suspend fun syncFlashcards() {
        val dirtyCards = flashcardDao.getDirtyCards()
        if (dirtyCards.isEmpty()) return

        android.util.Log.d("HUY_DEBUG", "SyncManager: Đang đẩy ${dirtyCards.size} thẻ lên Atlas...")

        dirtyCards.forEach { entity ->
            try {
                val cardDomain = entity.toDomain()
                val cardDto = cardDomain.toDto()

                // Phân luồng POST/PUT dựa trên lịch sử học (SM-2)
                val response = if (cardDomain.repetition == 0 && cardDomain.lastReviewedAt == null) {
                    flashcardApi.createFlashcard(testToken, cardDto)
                } else {
                    flashcardApi.updateFlashcard(testToken, cardDomain.id, cardDto)
                }

                if (response.isSuccessful) {
                    flashcardDao.insertOrUpdate(entity.copy(isDirty = false))
                    android.util.Log.d("HUY_DEBUG", "✅ Sync CARD thành công: ${entity.frontText}")
                }
            } catch (e: Exception) {
                android.util.Log.e("HUY_DEBUG", "❌ Lỗi sync CARD ${entity.id}: ${e.message}")
            }
        }
    }

    /**
     * 3. Lập lịch chạy ngầm qua WorkManager
     */
    fun scheduleSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Chỉ chạy khi có mạng
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<FlashcardSyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                WorkRequest.MIN_BACKOFF_MILLIS,
                java.util.concurrent.TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "data_sync_work",
            ExistingWorkPolicy.KEEP, // Giữ lại hàng chờ cũ nếu đang có sẵn
            syncRequest
        )
    }
}
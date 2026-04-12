package com.example.android.data.repository

import android.content.Context
import com.example.android.data.local.dao.FlashcardDao
import com.example.android.data.remote.FlashcardApi
import com.example.android.data.sync.DataSyncManager
import com.example.android.data.mapper.*
import com.example.android.domain.model.Flashcard
import com.example.android.domain.repository.FlashcardRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FlashcardRepositoryImpl @Inject constructor(
    private val flashcardDao: FlashcardDao,
    private val flashcardApi: FlashcardApi,
    private val syncManager: DataSyncManager,
    @ApplicationContext private val context: Context
) : FlashcardRepository {

    override fun getCardsByDeck(deckId: String): Flow<List<Flashcard>> =
        flashcardDao.getCardsByDeck(deckId).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun insertOrUpdate(flashcard: Flashcard) {
        // 1. Lưu Local trước
        flashcardDao.insertOrUpdate(flashcard.toEntity().copy(isDirty = true))

        try {
            val testToken = "Bearer eyJhbGci..." // Token của Huy

            val response = if (flashcard.repetition == 0 && flashcard.lastReviewedAt == null) {
                flashcardApi.createFlashcard(testToken, flashcard.toDto())
            } else {
                flashcardApi.updateFlashcard(testToken, flashcard.id, flashcard.toDto())
            }

            if (response.isSuccessful) {
                flashcardDao.insertOrUpdate(flashcard.toEntity().copy(isDirty = false))
                android.util.Log.d("HUY_DEBUG", "SYNC_OK: Atlas đã cập nhật CARD thành công!")
            } else {
                syncManager.scheduleSync()
            }
        } catch (e: Exception) {
            android.util.Log.e("HUY_DEBUG", "LỖI KẾT NỐI CARD: ${e.message}")
            syncManager.scheduleSync()
        }
    }

    override suspend fun softDelete(id: String) {
        flashcardDao.softDelete(id)
    }

    override suspend fun syncDirtyCards() {
        syncManager.syncFlashcards()
    }

    override fun scheduleSync() {
        syncManager.scheduleSync()
    }
}
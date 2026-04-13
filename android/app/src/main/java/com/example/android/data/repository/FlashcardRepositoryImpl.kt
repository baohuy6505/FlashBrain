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
import android.util.Log // 👈 Kiểm tra dòng này
class FlashcardRepositoryImpl @Inject constructor(
    private val flashcardDao: FlashcardDao,
    private val flashcardApi: FlashcardApi,
    private val syncManager: DataSyncManager,
    private val sessionManager: com.example.android.data.local.SessionManager,
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
            val token = sessionManager.authToken?.let {
                if (it.startsWith("Bearer ")) it else "Bearer $it"
            } ?: return

            val response = if (flashcard.repetition == 0 && flashcard.lastReviewedAt == null) {
                flashcardApi.createFlashcard(token, flashcard.toDto())
            } else {
                flashcardApi.updateFlashcard(token, flashcard.id, flashcard.toDto())
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

    // Thêm vào FlashcardRepositoryImpl
    override suspend fun fetchCardsFromServer(deckId: String) {
        try {
            val token = sessionManager.authToken?.let {
                if (it.startsWith("Bearer ")) it else "Bearer $it"
            } ?: return

            val response = flashcardApi.getFlashcardsByDeck(token, deckId)
            if (response.isSuccessful) {
                val remoteCards = response.body()?.data ?: emptyList()

                remoteCards.forEach { dto ->
                    // Lưu vào Room và đánh dấu là dữ liệu sạch (isDirty = false)
                    flashcardDao.insertOrUpdate(dto.toEntity().copy(isDirty = false))
                }
                Log.d("HUY_DEBUG", "PULL_CARDS_OK: Đã tải ${remoteCards.size} thẻ của Deck $deckId")
            }
        } catch (e: Exception) {
            Log.e("HUY_DEBUG", "LỖI PULL_CARDS: ${e.message}")
        }
    }
}
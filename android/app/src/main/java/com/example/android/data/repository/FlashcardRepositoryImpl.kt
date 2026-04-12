package com.example.android.data.repository

import com.example.android.data.local.dao.FlashcardDao
import com.example.android.data.local.entity.FlashcardEntity
import com.example.android.data.remote.FlashcardApi
import com.example.android.domain.model.Deck
import com.example.android.domain.model.Flashcard
import com.example.android.domain.repository.FlashcardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FlashcardRepositoryImpl @Inject constructor(
    private val flashcardDao: FlashcardDao,
    private val flashcardApi: FlashcardApi
) : FlashcardRepository {

    override fun getCardsByDeck(deckId: String): Flow<List<Flashcard>> =
        flashcardDao.getCardsByDeck(deckId).map { entities ->
            entities.map { it.toDomain() }
        }

    // FlashcardRepositoryImpl.kt
    override suspend fun insertOrUpdate(flashcard: Flashcard) {
        // 1. Lưu vào Room trước (Offline-first)
        flashcardDao.insertOrUpdate(flashcard.toEntity().copy(isDirty = true))

        // 2. Gọi API để lưu lên Server
        try {
            val testToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJhYzVmMjllOC0yZDdmLTQ4YzEtODA0OC05OTI3YWNiODk1OTYiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc3NjAwMzk2MCwiZXhwIjoxNzc2NjA4NzYwfQ.6rAFsJGvJ2JdsYfABR-QUht_3H7QrOpPmMaGeYN9v18"

            val response = flashcardApi.createFlashcard(testToken, flashcard.toDto())

            if (response.isSuccessful) {
                // 3. Nếu thành công thì bỏ đánh dấu Dirty
                flashcardDao.insertOrUpdate(flashcard.toEntity().copy(isDirty = false))
                println("SYNC_OK: Đã lưu lên Node.js thành công!")
            } else {
                // In ra lỗi chi tiết từ server (ví dụ: 400, 401, 500)
                println("SYNC_FAIL: Server trả về lỗi ${response.code()} - ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            println("SYNC_ERROR: Không kết nối được Server: ${e.message}")
        }
    }

    override suspend fun softDelete(id: String) {
        flashcardDao.softDelete(id)
    }
}

//mapper du lieu giua 2 model
// Mapper từ Entity (DB) sang Domain (UI)
fun FlashcardEntity.toDomain() = Flashcard(
    id = id,
    deckId = deckId,
    frontText = frontText,
    backText = backText,
    interval = interval,
    repetition = repetition,
    easeFactor = easeFactor,
    nextReviewDate = nextReviewDate,
    lastReviewedAt = lastReviewedAt,
    isDeleted = isDeleted,
    createdAt = createdAt,
    updatedAt = updatedAt
)

// Mapper từ Domain (UI) sang Entity (DB)
fun Flashcard.toEntity() = FlashcardEntity(
    id = id,
    deckId = deckId,
    frontText = frontText,
    backText = backText,
    interval = interval,
    repetition = repetition,
    easeFactor = easeFactor,
    nextReviewDate = nextReviewDate,
    lastReviewedAt = lastReviewedAt,
    isDeleted = isDeleted,
    isDirty = false,
    createdAt = createdAt,
    updatedAt = updatedAt
)
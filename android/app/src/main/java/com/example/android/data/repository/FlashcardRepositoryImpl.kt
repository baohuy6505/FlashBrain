package com.example.android.data.repository

import com.example.android.data.local.dao.FlashcardDao
import com.example.android.data.local.entity.FlashcardEntity
import com.example.android.domain.model.Deck
import com.example.android.domain.model.Flashcard
import com.example.android.domain.repository.FlashcardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FlashcardRepositoryImpl @Inject constructor(
    private val flashcardDao: FlashcardDao
) : FlashcardRepository {

    override fun getCardsByDeck(deckId: String): Flow<List<Flashcard>> =
        flashcardDao.getCardsByDeck(deckId).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun insertOrUpdate(flashcard: Flashcard) {
        flashcardDao.insertOrUpdate(flashcard.toEntity().copy(isDirty = true))
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
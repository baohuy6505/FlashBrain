package com.example.android.data.mapper

import com.example.android.data.local.entity.FlashcardEntity
import com.example.android.data.remote.dto.FlashcardDto
import com.example.android.domain.model.Flashcard

// --- Mapper từ Entity (Room) sang Domain (UI) ---
fun FlashcardEntity.toDomain() = Flashcard(
    id = id,
    deckId = deckId,
    frontText = frontText,
    backText = backText,
    interval = interval,
    repetition = repetition,
    easeFactor = easeFactor,
    nextReviewDate = nextReviewDate ?: "",
    lastReviewedAt = lastReviewedAt,
    isDeleted = isDeleted,
    createdAt = createdAt,
    updatedAt = updatedAt
)

// --- Mapper từ Domain (UI) sang Entity (Room) ---
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

// --- Mapper từ Domain sang DTO (Gửi lên Server) ---
fun Flashcard.toDto() = FlashcardDto(
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

// --- Mapper từ DTO (Server) sang Entity (Room) ---
fun FlashcardDto.toEntity() = FlashcardEntity(
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
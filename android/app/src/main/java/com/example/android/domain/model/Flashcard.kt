package com.example.android.domain.model

data class Flashcard(
    val id: String,
    val deckId: String,
    val frontText: String,
    val backText: String,

    // Các chỉ số phục vụ thuật toán SM-2
    val interval: Int = 0,
    val repetition: Int = 0,
    val easeFactor: Double = 2.5,
    val nextReviewDate: String? = null,

    val lastReviewedAt: String? = null,
    val isDeleted: Boolean = false,
    val createdAt: String,
    val updatedAt: String
)
data class DeckWithCards(
    val deck: Deck,
    val flashcards: List<Flashcard>
)
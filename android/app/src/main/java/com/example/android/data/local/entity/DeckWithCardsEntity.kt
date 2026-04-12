package com.example.android.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class DeckWithCardsEntity(
    @Embedded val deck: DeckEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "deckId"
    )
    val flashcards: List<FlashcardEntity>
)
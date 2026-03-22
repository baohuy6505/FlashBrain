package com.example.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey val id: String, // UUID
    val deckId: String,
    val frontText: String,
    val backText: String,
    val interval: Int,
    val updatedAt: Long
)

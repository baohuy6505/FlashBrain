package com.example.android.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
@Entity(
    tableName = "flashcards",
    foreignKeys = [
        ForeignKey(
            entity = DeckEntity::class,
            parentColumns = ["id"],
            childColumns = ["deckId"],
            onDelete = ForeignKey.CASCADE // Xóa Deck là xóa sạch Card bên trong
        )
    ],
    indices = [Index(value = ["deckId"])] //đánh index để truy vấn theo deckId cho nhanh
)
data class FlashcardEntity(
    @PrimaryKey val id: String,
    val deckId: String,
    val frontText: String,
    val backText: String,

    // SM-2 fields
    val interval: Int,
    val repetition: Int,
    val easeFactor: Double,
    val nextReviewDate: Long?,

    val lastReviewedAt: Long?,
    val isDeleted: Boolean = false,
    val isDirty: Boolean = false, // Phục vụ Sync Node.js
    val createdAt: Long,
    val updatedAt: Long
)
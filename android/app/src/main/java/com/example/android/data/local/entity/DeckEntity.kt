package com.example.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decks")
data class DeckEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val isPublic: Boolean,
    val isDeleted: Boolean,
    val isDirty: Boolean,
    val serverId: String?,
    val createdAt: String,
    val updatedAt: String
)
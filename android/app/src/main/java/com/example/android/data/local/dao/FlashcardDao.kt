package com.example.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.data.local.entity.FlashcardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcards WHERE deckId = :deckId AND isDeleted = 0 ORDER BY createdAt DESC")
    fun getCardsByDeck(deckId: String): Flow<List<FlashcardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(card: FlashcardEntity)

    @Query("UPDATE flashcards SET isDeleted = 1, updatedAt = :timestamp WHERE id = :id")
    suspend fun softDelete(id: String, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT * FROM flashcards WHERE isDirty = 1")
    suspend fun getDirtyCards(): List<FlashcardEntity>
}
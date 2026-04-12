package com.example.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.data.local.entity.DeckEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {
    @Query("SELECT * FROM decks WHERE isDeleted = 0 ORDER BY updatedAt DESC")
    fun getDecks(): Flow<List<DeckEntity>>

    @Query("SELECT * FROM decks WHERE id = :id LIMIT 1")
    fun getDeckById(id: String): Flow<DeckEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(deck: DeckEntity)

    @Query("SELECT * FROM decks WHERE isDirty = 1")
    suspend fun getDirtyDecks(): List<DeckEntity>

    @Query("UPDATE decks SET isDeleted = 1, isDirty = 1, updatedAt = :time WHERE id = :id")
    suspend fun softDelete(id: String, time: Long = System.currentTimeMillis())
}
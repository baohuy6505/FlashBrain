package com.example.android.domain.repository

import com.example.android.domain.model.Deck
import kotlinx.coroutines.flow.Flow

interface DeckRepository {
    val allDecks: Flow<List<Deck>>
    fun getDeckById(id: String): Flow<Deck?>
    suspend fun saveDeck(deck: Deck)
    suspend fun deleteDeck(id: String)
}
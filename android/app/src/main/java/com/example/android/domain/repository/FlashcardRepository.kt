package com.example.android.domain.repository

import com.example.android.domain.model.Deck
import com.example.android.domain.model.Flashcard
import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {
    //lay danh sach the theo ID cua Deck
    fun getCardsByDeck(deckId: String): Flow<List<Flashcard>>

    //luu hoac cap nhat the
    suspend fun insertOrUpdate(flashcard: Flashcard)

    //xoa the bang id
    suspend fun softDelete(id: String)
}
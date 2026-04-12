package com.example.android.data.repository

import com.example.android.data.local.dao.DeckDao
import com.example.android.data.local.entity.DeckEntity
import com.example.android.domain.model.Deck
import com.example.android.domain.repository.DeckRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DeckRepositoryImpl @Inject constructor(
    private val deckDao: DeckDao
): DeckRepository {

    override val allDecks: Flow<List<Deck>> = deckDao.getDecks().map { entities ->
        entities.map { it.toDomain() }
    }

    override fun getDeckById(id: String): Flow<Deck?> =
        deckDao.getDeckById(id).map{entity ->
            entity?.toDomain()
        }

    override suspend fun saveDeck(deck: Deck) {
        deckDao.insertOrUpdate(deck.toEntity())
    }

    override suspend fun deleteDeck(id: String) {
        deckDao.softDelete(id)
    }
}

// Hàm Mapper (Tạo file riêng hoặc để trong Repository)
fun DeckEntity.toDomain() = Deck(id, userId, title, isPublic, isDeleted, createdAt, updatedAt, isDirty, serverId)
fun Deck.toEntity() = DeckEntity(id, userId, title, isPublic, isDeleted, isDirty, serverId, createdAt, updatedAt)
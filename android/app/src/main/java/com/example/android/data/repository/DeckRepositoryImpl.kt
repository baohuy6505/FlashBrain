package com.example.android.data.repository

import com.example.android.data.local.dao.DeckDao
import com.example.android.data.remote.FlashcardApi
import com.example.android.data.sync.DataSyncManager
import com.example.android.data.mapper.*
import com.example.android.domain.model.Deck
import com.example.android.domain.repository.DeckRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeckRepositoryImpl @Inject constructor(
    private val deckDao: DeckDao,
    private val flashcardApi: FlashcardApi,
    private val syncManager: DataSyncManager // 👈 Thêm Manager vào đây
): DeckRepository {

    override val allDecks: Flow<List<Deck>> = deckDao.getDecks().map { entities ->
        entities.map { it.toDomain() }
    }

    override fun getDeckById(id: String): Flow<Deck?> =
        deckDao.getDeckById(id).map { entity ->
            entity?.toDomain()
        }

    override suspend fun saveDeck(deck: Deck) {
        // 1. Lưu vào Room trước với trạng thái isDirty = true
        deckDao.insertOrUpdate(deck.toEntity().copy(isDirty = true))

        try {
            val testToken = "Bearer eyJhbGci..." // Token của Huy

            // Chuyển sang DTO và gửi
            val response = flashcardApi.createDeck(testToken, deck.toDto())

            if (response.isSuccessful) {
                // 2. Thành công -> Tắt cờ Dirty
                deckDao.insertOrUpdate(deck.toEntity().copy(isDirty = false))
                android.util.Log.d("HUY_DEBUG", "SYNC_OK: Atlas đã cập nhật DECK thành công!")
            } else {
                // 3. Server lỗi -> Gọi Worker chờ
                syncManager.scheduleSync()
            }
        } catch (e: Exception) {
            android.util.Log.e("HUY_DEBUG", "LỖI KẾT NỐI DECK: ${e.message}")
            // 4. Mất mạng -> Gọi Worker chờ
            syncManager.scheduleSync()
        }
    }

    override suspend fun deleteDeck(id: String) {
        deckDao.softDelete(id)
    }
}
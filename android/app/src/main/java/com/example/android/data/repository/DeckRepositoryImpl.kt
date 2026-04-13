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
import android.util.Log // 👈 Kiểm tra dòng này
class DeckRepositoryImpl @Inject constructor(
    private val deckDao: DeckDao,
    private val flashcardApi: FlashcardApi,
    private val syncManager: DataSyncManager,
    private val sessionManager: com.example.android.data.local.SessionManager
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
            val token = sessionManager.authToken?.let {
                if (it.startsWith("Bearer ")) it else "Bearer $it"
            } ?: return

            // Chuyển sang DTO và gửi
            val response = flashcardApi.createDeck(token, deck.toDto())

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
    // Thêm vào DeckRepositoryImpl
    override suspend fun fetchDecksFromServer() {
        try {
            val token = sessionManager.authToken?.let {
                if (it.startsWith("Bearer ")) it else "Bearer $it"
            } ?: return

            val response = flashcardApi.getAllDecks(token) // Đảm bảo FlashcardApi đã có hàm này
            if (response.isSuccessful) {
                val remoteDecks = response.body()?.data ?: emptyList()

                // Chuyển DTO sang Entity và lưu vào Room
                remoteDecks.forEach { dto ->
                    // isDirty = false vì dữ liệu này vừa lấy từ Server về, đã khớp 100%
                    deckDao.insertOrUpdate(dto.toEntity().copy(isDirty = false))
                }
                Log.d("HUY_DEBUG", "PULL_DECKS_OK: Đã tải ${remoteDecks.size} bộ thẻ về máy.")
            }
        } catch (e: Exception) {
            Log.e("HUY_DEBUG", "LỖI PULL_DECKS: ${e.message}")
        }
    }
    override suspend fun deleteDeck(id: String) {
        deckDao.softDelete(id)
    }
}
package com.example.android.presentation.screens.desk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.data.local.SessionManager // Hoặc TokenManager tùy Huy đặt tên
import com.example.android.domain.model.Deck
import com.example.android.domain.repository.DeckRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DeckViewModel @Inject constructor(
    private val repository: DeckRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    val currentUserId: String = sessionManager.currentUser?.id ?: "guest"
    val decksState: StateFlow<List<Deck>> = repository.allDecks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addOrUpdateDeck(existingDeck: Deck? = null, title: String, isPublic: Boolean) {
        val currentUserId = sessionManager.currentUser?.id ?: "guest"
        val currentTime = System.currentTimeMillis().toString()

        viewModelScope.launch {
            val deckToSave = if (existingDeck != null) {
                existingDeck.copy(
                    title = title,
                    isPublic = isPublic,
                    isDirty = true,
                    updatedAt = currentTime
                )
            } else {
                Deck(
                    id = UUID.randomUUID().toString(),
                    userId = currentUserId,
                    title = title,
                    isPublic = isPublic,
                    isDirty = true,
                    serverId = null, // Chưa có trên server
                    createdAt = currentTime,
                    updatedAt = currentTime,
                    isDeleted = false
                )
            }

            repository.saveDeck(deckToSave)
        }
    }

    fun deleteDeck(deckId: String) {
        viewModelScope.launch {
            repository.deleteDeck(deckId)
        }
    }
}
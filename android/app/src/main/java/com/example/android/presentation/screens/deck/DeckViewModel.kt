package com.example.android.presentation.screens.flashcard

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.data.local.SessionManager
import com.example.android.domain.model.Deck
import com.example.android.domain.repository.DeckRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DeckViewModel @Inject constructor(
    private val repository: DeckRepository,
    private val sessionManager: SessionManager  //lay thong tin khi login
) : ViewModel() {

    //chuyen doi flow tu room thanh stateflow, statein giup giu data lai khi xoay man hinh
    val decksState: StateFlow<List<Deck>> = repository.allDecks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), //doi 5s truoc khi dung neu ui dong
        initialValue = emptyList()
    )

    fun addOrUpdateDeck(id: String?, title: String, isPublic: Boolean) {
        val currentUserId = sessionManager.currentUser?.id ?: "guest"
        viewModelScope.launch {
            val newDeck = Deck(
                id = id ?: UUID.randomUUID().toString(), //tao moi uuid
                userId = currentUserId, //id của user login
                title = title,
                isPublic = isPublic
            )
            repository.saveDeck(newDeck)
        }
    }

    fun deleteDeck(deckId: String) {
        viewModelScope.launch {
            repository.deleteDeck(deckId)
        }
    }
}
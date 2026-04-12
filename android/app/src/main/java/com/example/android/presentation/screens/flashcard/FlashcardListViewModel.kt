package com.example.android.presentation.screens.flashcard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.domain.model.Deck
import com.example.android.domain.model.Flashcard
import com.example.android.domain.repository.DeckRepository
import com.example.android.domain.repository.FlashcardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class FlashcardViewModel @Inject constructor(
    private val flashcardRepository: FlashcardRepository,
    private val deckRepository: DeckRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    //lay deckId tu Navigation truy cho
    private val deckId: String = savedStateHandle.get<String>("deckId") ?: ""
    // 1. Quản lý ID của bộ thẻ hiện tại
    private val _currentDeckId = MutableStateFlow("")

    // 2. Tự động lấy thông tin Deck mỗi khi ID thay đổi (và khác rỗng)
    val deckInfo = if (deckId.isEmpty()) flowOf(null)
    else deckRepository.getDeckById(deckId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val uiState = if (deckId.isEmpty()) flowOf(emptyList())
    else flashcardRepository.getCardsByDeck(deckId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _cardsToReview = MutableStateFlow<List<Flashcard>>(emptyList())
    val cardsToReview = _cardsToReview.asStateFlow()

    // 4. Hàm này được gọi từ LaunchedEffect bên màn hình UI
    fun setDeckId(id: String) {
        _currentDeckId.value = id
    }

    // 5.thêm và sửa flashcard
    fun addOrUpdateCard(existingCard: Flashcard?, front: String, back: String) {
        val currentId = _currentDeckId.value
        if (currentId.isEmpty()) return

        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val flashcard = if(existingCard != null){
                existingCard.copy(
                    frontText = front,
                    backText = back,
                    updatedAt = now
                )
            } else {
                Flashcard(
                    id = UUID.randomUUID().toString(),
                    deckId = currentId, //sử dụng currentId ở đây
                    frontText = front,
                    backText = back,
                    interval = 0,
                    repetition = 0,
                    easeFactor = 2.5,
                    nextReviewDate = now,
                    lastReviewedAt = null,
                    createdAt = now,
                    updatedAt = now
                )
            }
            flashcardRepository.insertOrUpdate(flashcard)
        }
    }

    //xóa mềm flashcard
    fun deleteCard(cardId: String) {
        viewModelScope.launch {
            flashcardRepository.softDelete(cardId)
        }
    }

    fun loadCardsToReview(deckId: String) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            flashcardRepository.getCardsByDeck(deckId).collect { allCards ->
                val reviewList = allCards.filter {
                    (it.nextReviewDate ?: 0L) <= now && !it.isDeleted
                }
                _cardsToReview.value = reviewList
            }
        }
    }
}
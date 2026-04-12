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
    init {
        flashcardRepository.scheduleSync()
    }
    //lay deckId tu Navigation truy cho
    private val deckId: String = savedStateHandle.get<String>("deckId") ?: ""
    // 1. Quản lý ID của bộ thẻ hiện tại
    val deckInfo = if (deckId.isEmpty()) flowOf(null)

    // 2. Tự động lấy thông tin Deck mỗi khi ID thay đổi (và khác rỗng)
    else deckRepository.getDeckById(deckId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val uiState = if (deckId.isEmpty()) flowOf(emptyList())
    else flashcardRepository.getCardsByDeck(deckId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _cardsToReview = MutableStateFlow<List<Flashcard>>(emptyList())
    val cardsToReview = _cardsToReview.asStateFlow()



    // 5.thêm và sửa flashcard
    fun addOrUpdateCard(existingCard: Flashcard?, front: String, back: String) {
        if (deckId.isEmpty()) {
            android.util.Log.e("HUY_DEBUG", "Lỗi: deckId từ Navigation bị rỗng!")
            return
        }

        viewModelScope.launch {
            val nowIso = java.time.Instant.now().toString()
            val flashcard = if(existingCard != null){
                existingCard.copy(
                    frontText = front,
                    backText = back,
                    updatedAt = java.time.Instant.now().toString(),
                )
            } else {
                Flashcard(
                    id = UUID.randomUUID().toString(),
                    deckId = deckId, //sử dụng currentId ở đây
                    frontText = front,
                    backText = back,
                    interval = 0,
                    repetition = 0,
                    easeFactor = 2.5,
                    nextReviewDate = nowIso,
                    lastReviewedAt = null,
                    createdAt = nowIso, // Kết quả: "2026-04-12T..."
                    updatedAt = nowIso,
                )
            }
            android.util.Log.d("HUY_DEBUG", "Gửi Flashcard với deckId: $deckId")
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
            val nowIso = java.time.Instant.now().toString()
            flashcardRepository.getCardsByDeck(deckId).collect { allCards ->
                val reviewList = allCards.filter {
                    val isDue = it.nextReviewDate != null && it.nextReviewDate <= nowIso
                    isDue && !it.isDeleted
                }
                _cardsToReview.value = reviewList
            }
        }
    }
}
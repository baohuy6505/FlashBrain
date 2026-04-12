package com.example.android.presentation.screens.study

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.domain.model.Flashcard
import com.example.android.domain.repository.FlashcardRepository
import com.example.android.domain.util.SM2Engine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class StudyViewModel @Inject constructor(
    private val flashcardRepository: FlashcardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val deckId: String = savedStateHandle.get<String>("deckId") ?: ""

    // 1. Quản lý danh sách ID những thẻ đã nhấn nút trong phiên này
    private val answeredIds = MutableStateFlow<Set<String>>(emptySet())

    private val _isLoopingMode = MutableStateFlow(true)
    val isLoopingMode: StateFlow<Boolean> = _isLoopingMode.asStateFlow()

    // Trong StudyViewModel.kt
    val cardsToReview: StateFlow<List<Flashcard>> = combine(
        flashcardRepository.getCardsByDeck(deckId),
        answeredIds
    ) { allCards, answered ->
        val activeCards = allCards.filter { !it.isDeleted }
        val now = java.time.Instant.now().toString()

        _isLoopingMode.value = activeCards.size < 10
        val remainingCards = if (_isLoopingMode.value) {
            // Chế độ Looping: Chỉ lọc những thẻ chưa nhấn nút trong lượt này
            activeCards.filter { it.id !in answered }
        } else {
            // Chế độ Học thật: Lọc thẻ đến hạn (String <= String) và chưa nhấn nút
            activeCards.filter { (it.nextReviewDate ?: "") <= now && it.id !in answered }
        }

        if (remainingCards.isEmpty() && activeCards.isNotEmpty() && activeCards.size < 10) {
            answeredIds.value = emptySet()
            activeCards
        } else {
            remainingCards
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

   //su dung thuat toan SM2
    fun answerCard(card: Flashcard, quality: Int) {
        viewModelScope.launch {
           val updatedCard = SM2Engine.calculate(card, quality)
            println("CHECK_SM2: Card=${card.frontText} | Q=$quality -> Next Interval: ${updatedCard.interval} days")
            flashcardRepository.insertOrUpdate(updatedCard)
            answeredIds.update { it + card.id }
        }
    }

    fun resetSession() {
        answeredIds.value = emptySet()
    }
}
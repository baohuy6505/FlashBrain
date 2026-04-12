package com.example.android.presentation.screens.study

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.domain.model.Flashcard
import com.example.android.domain.repository.FlashcardRepository
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
    @OptIn(ExperimentalCoroutinesApi::class)
    val cardsToReview: StateFlow<List<Flashcard>> = flow {
        val now = System.currentTimeMillis()
        if (deckId.isNotEmpty()) {
            flashcardRepository.getCardsByDeck(deckId).collect { allCards ->
                val reviewList = allCards.filter {
                    !it.isDeleted && (it.nextReviewDate ?: 0L) <= now
                }
                emit(reviewList)
            }
        } else {
            emit(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    /**
     * 4. LOGIC QUAN TRỌNG: Cập nhật thẻ sau khi học (Thuật toán SM-2)
     * @param quality: Mức độ nhớ (0: Quên hoàn toàn, 1: Khó, 2: Được, 3: Dễ)
     */
    fun answerCard(card: Flashcard, quality: Int) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()

            // Logic tính toán Interval (Khoảng cách ngày) và EaseFactor (Độ dễ)
            var newRepetition = card.repetition
            var newEaseFactor = card.easeFactor
            var newInterval: Int

            if (quality >= 2) { // Nếu trả lời đúng (Được hoặc Dễ)
                if (newRepetition == 0) {
                    newInterval = 1 // Lần đầu đúng: 1 ngày sau xem lại
                } else if (newRepetition == 1) {
                    newInterval = 6 // Lần hai đúng: 6 ngày sau xem lại
                } else {
                    // Các lần sau: Khoảng cách = Khoảng cách cũ * Độ dễ
                    newInterval = (card.interval * card.easeFactor).toInt()
                }
                newRepetition += 1
            } else { // Nếu trả lời sai hoặc quá khó
                newRepetition = 0
                newInterval = 1
            }

            // Cập nhật EaseFactor: Điều chỉnh độ khó dựa trên phản hồi của user
            // Công thức rút gọn của SM-2
            newEaseFactor += (0.1 - (3 - quality) * (0.08 + (3 - quality) * 0.02))
            if (newEaseFactor < 1.3) newEaseFactor = 1.3 // Không để độ dễ thấp quá

            // Tính toán ngày ôn tiếp theo (Đổi Interval từ ngày sang miliseconds)
            val oneDayMs = 24 * 60 * 60 * 1000L
            val nextReviewDate = now + (newInterval * oneDayMs)

            // Lưu vào Database
            val updatedCard = card.copy(
                repetition = newRepetition,
                easeFactor = newEaseFactor,
                interval = newInterval,
                nextReviewDate = nextReviewDate,
                lastReviewedAt = now,
                updatedAt = now
            )
            flashcardRepository.insertOrUpdate(updatedCard)
        }
    }

    //ham thuat toan SM
//    fun answerCard(card: Flashcard, quality: Int) {
//        viewModelScope.launch {
//            val now = System.currentTimeMillis()
//
//            var newRepetition = card.repetition
//            var newEaseFactor = card.easeFactor
//            var newInterval: Int
//
//            if (quality >= 2) { // Trả lời đúng
//                if (newRepetition == 0) {
//                    newInterval = 1
//                } else if (newRepetition == 1) {
//                    newInterval = 6
//                } else {
//                    newInterval = (card.interval * card.easeFactor).toInt()
//                }
//                newRepetition += 1
//            } else { // Trả lời sai
//                newRepetition = 0
//                newInterval = 1
//            }
//
//            // Cập nhật EaseFactor (SM-2 Formula)
//            newEaseFactor += (0.1 - (3 - quality) * (0.08 + (3 - quality) * 0.02))
//            if (newEaseFactor < 1.3) newEaseFactor = 1.3
//
//            val oneDayMs = 24 * 60 * 60 * 1000L
//            val nextReviewDate = now + (newInterval * oneDayMs)
//
//            val updatedCard = card.copy(
//                repetition = newRepetition,
//                easeFactor = newEaseFactor,
//                interval = newInterval,
//                nextReviewDate = nextReviewDate,
//                lastReviewedAt = now,
//                updatedAt = now
//            )
//            flashcardRepository.insertOrUpdate(updatedCard)
//        }
//    }
}
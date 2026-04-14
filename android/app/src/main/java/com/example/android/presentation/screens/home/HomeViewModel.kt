package com.example.android.presentation.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.domain.repository.DeckRepository
import com.example.android.domain.repository.FlashcardRepository
import com.example.android.domain.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val progressRepository: ProgressRepository,
    private val flashcardRepository: FlashcardRepository,
    private val deckRepository: DeckRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _syncError = MutableSharedFlow<String>()
    val syncError: SharedFlow<String> = _syncError.asSharedFlow()
    init {
        observeProgress()
        // Gọi hàm kéo dữ liệu ngay khi vào màn hình Home
        fetchData()
    }
    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Kéo tất cả về: Deck, Progress
                deckRepository.fetchDecksFromServer()
                progressRepository.syncFromServer()

                Log.d("HUY_DEBUG", "SYNC_RESUME_OK: Đã tự động cập nhật dữ liệu mới nhất!")
            } catch (e: Exception) {
                Log.e("HUY_DEBUG", "SYNC_RESUME_FAIL: ${e.message}")
            }
        }
    }

    private fun observeProgress() {
        progressRepository.getProgress()
            .filterNotNull()
            .onEach { progress ->
                _state.update {
                    it.copy(
                        streakDays = progress.streakDays,
                        totalCardsLearned = progress.totalLearned,
                        lastSessionText = if (progress.lastStudyDate != null) "Hôm nay" else "Chưa học"
                    )
                }
            }
            .launchIn(viewModelScope)
    }
//    private val _state = MutableStateFlow(HomeState())
//    val state: StateFlow<HomeState> = _state.asStateFlow()
//
//    private val _syncError = MutableSharedFlow<String>()
//    val syncError: SharedFlow<String> = _syncError.asSharedFlow()



    // 5. Sync server
    fun syncFromServer() {
        viewModelScope.launch {
            progressRepository.syncFromServer()
                .onFailure { _syncError.emit("Không thể cập nhật dữ liệu mới nhất") }
        }
    }

    // Helper: Tính số thẻ đã học mỗi ngày trong tuần
    private fun buildWeeklyStats(reviewTimestamps: List<Long>): List<WeeklyStat> {
        return (0..6).map { offset ->
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -(6 - offset))

            val startOfDay = cal.apply {
                set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val endOfDay = startOfDay + 86_400_000L
            val count = reviewTimestamps.count { it in startOfDay until endOfDay }

            val label = when (cal.get(Calendar.DAY_OF_WEEK)) {
                Calendar.MONDAY    -> "T2"
                Calendar.TUESDAY   -> "T3"
                Calendar.WEDNESDAY -> "T4"
                Calendar.THURSDAY  -> "T5"
                Calendar.FRIDAY    -> "T6"
                Calendar.SATURDAY  -> "T7"
                else               -> "CN"
            }

            WeeklyStat(
                day = label,
                count = count,
                isToday = offset == 6
            )
        }
    }
}
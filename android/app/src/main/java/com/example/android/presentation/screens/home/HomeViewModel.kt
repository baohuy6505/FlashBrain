package com.example.android.presentation.screens.home

import androidx.compose.ui.graphics.Color // Đảm bảo dùng Color của Compose
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        _state.value = HomeState(
            streakDays = 20,
            totalCardsLearned = 1500,
            lastSessionText = "Today",
            reviewCardCount = 15,
            recentDecks = listOf(
                DeckMock("1", "IELTS Academic", 450, Color(0xFFC0D3FF)),
                DeckMock("2", "Daily Phrases", 120, Color(0xFFFFD591))
            ),

            weeklyStats = listOf(
                WeeklyStat("T2", 20),
                WeeklyStat("T3", 35),
                WeeklyStat("T4", 10),
                WeeklyStat("T5", 60,  isToday = true),
                WeeklyStat("T6", 25),
                WeeklyStat("T7", 90, isToday = true),
                WeeklyStat("CN", 0)
            )
        )
    }
}
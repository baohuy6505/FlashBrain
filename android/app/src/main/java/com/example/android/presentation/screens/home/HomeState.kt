package com.example.android.presentation.screens.home

import androidx.compose.ui.graphics.Color

// 1. Khuôn cho mỗi bộ thẻ
data class DeckMock(
    val id: String,
    val title: String,
    val cardCount: Int,
    val iconColor: Color
)

// 2. Khuôn cho biểu đồ thống kê
data class WeeklyStat(
    val day: String,
    val count: Int,
    val isToday: Boolean = false
)

// 3. Trạng thái tổng của màn hình Home
data class HomeState(
    val streakDays: Int = 0,
    val totalCardsLearned: Int = 0,
    val lastSessionText: String = "",
    val reviewCardCount: Int = 0,
    val recentDecks: List<DeckMock> = emptyList(),
    val isProUser: Boolean = false,
    val weeklyStats: List<WeeklyStat> = emptyList()
)
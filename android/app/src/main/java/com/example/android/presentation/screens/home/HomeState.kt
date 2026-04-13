package com.example.android.presentation.screens.home

import com.example.android.domain.model.Deck

// Khuôn cho biểu đồ thống kê
data class WeeklyStat(
    val day: String,
    val count: Int,
    val isToday: Boolean = false
)

// Trạng thái tổng của màn hình Home
data class HomeState(
    val streakDays: Int = 0,
    val totalCardsLearned: Int = 0,
    val lastSessionText: String = "",
    val reviewCardCount: Int = 0,
    val recentDecks: List<Deck> = emptyList(), // Chuẩn bài!
    val isProUser: Boolean = false,
    val weeklyStats: List<WeeklyStat> = emptyList()
)
package com.example.android.presentation.screens.home

import androidx.compose.ui.graphics.Color

data class DeckMock(
    val id: String,
    val title: String,
    val cardCount: Int,
    val iconColor: Color
)

// Tạo sẵn danh sách demo để dùng luôn
val dummyDecks = listOf(
    DeckMock("1", "English Vocabulary", 45, Color(0xFF4CAF50)),
    DeckMock("2", "Android Interview", 20, Color(0xFF2196F3)),
    DeckMock("3", "Japanese N4", 120, Color(0xFFFF9800)),
    DeckMock("4", "Kotlin Coroutines", 15, Color(0xFF9C27B0))
)